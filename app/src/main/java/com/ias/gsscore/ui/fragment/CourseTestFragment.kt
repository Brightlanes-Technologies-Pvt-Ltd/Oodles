package com.ias.gsscore.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.ias.gsscore.DownloadBrochureInterface
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentCourseTestBinding
import com.ias.gsscore.downloadfileswithworkmanager.CHANNEL_DESC
import com.ias.gsscore.downloadfileswithworkmanager.NOTIFICATION_ID
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.network.response.myaccount.AllTestResponse
import com.ias.gsscore.ui.adapter.*
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.cart.CartItemResponse
import com.ias.gsscore.network.response.courses.BatchesList
import com.ias.gsscore.network.response.myaccount.MainsTest
import com.ias.gsscore.network.response.myaccount.ProgramList
import com.ias.gsscore.ui.activity.MainActivity
import com.ias.gsscore.ui.activity.OrderSummaryActivity
import com.ias.gsscore.ui.adapter.MainTestListAdapter.UploadAnswerInterface
import com.ias.gsscore.ui.bottomsheet.CourseTypeBottomSheet
import com.ias.gsscore.uploadFiles.PDFUploadListener
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.InterfaceClickListener
import com.ias.gsscore.utils.SingletonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CourseTestFragment(
    context: Context,
    var type: String,
    private val boolWhereFrom: Boolean,
    private var packageId: String
) : Fragment(),
    InterfaceClickListener,
    DownloadBrochureInterface,
    ViewAllBatchesListAdapter.EnrollClick,
    CourseTypeBottomSheet.BottomSheetCourseTypeClick,
    ViewAllBatchesListAdapter.BrochureClick {

    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    lateinit var binding: FragmentCourseTestBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    var materialTopList = ArrayList<MaterialResponse>();
    private var ptTestListAdapter: PtTestListAdapter? = null
    private var mainTestListAdapter: MainTestListAdapter? = null
    private var eBooksListAdapter: EBooksListAdapter? = null
    private var videosListAdapter: VideosListAdapter? = null
    private var scheduleListAdapter: ScheduleListAdapter? = null
    private var commentsListAdapter: CommentsListAdapter? = null
    private var programListAdapter: ProgramListAdapter? = null
    private var viewAllBatchesListAdapter: ViewAllBatchesListAdapter? = null
    private var clickListener: InterfaceClickListener? = null
    private var programId = ""
    private lateinit var downloadBrochureInterface: DownloadBrochureInterface
    lateinit var workManager: WorkManager
    private var selectedPosCourseType = -1
    private var bottomSheetCourseType: CourseTypeBottomSheet? = null
    private lateinit var enrollContext: ViewAllBatchesListAdapter.EnrollClick
    private lateinit var brochureClickContext: ViewAllBatchesListAdapter.BrochureClick
    private lateinit var interfaceCourseTypeContex: CourseTypeBottomSheet.BottomSheetCourseTypeClick
    private lateinit var uploadAnswerInterface: UploadAnswerInterface
    private var listener: PDFUploadListener? = null
    

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(context is PDFUploadListener) {
            listener = context as PDFUploadListener
        }
    }



    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course_test, container, false)
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        downloadBrochureInterface = this
        enrollContext = this
        interfaceCourseTypeContex = this
        brochureClickContext = this
        workManager = WorkManager.getInstance(requireActivity())
        initialData()
        return binding.root;

    }

    private fun initialData() {
        clickListener = this
        if (boolWhereFrom) {
            binding.layoutTvName.visibility = View.GONE
        }
        if (type == "My Downloads") binding.tvTitle.text = "Downloaded Videos"
        else binding.tvTitle.text = type
        linearLayoutManager = LinearLayoutManager(context)
        binding.rvMaterial.layoutManager = linearLayoutManager

        uploadAnswerInterface = object : UploadAnswerInterface {
            override fun uploadAnswer(data: MainsTest) {
                (listener as MainActivity).selectPDF(data)
            }

            override fun viewEvaluatedCopy(evaluatedPdf: String?) {
                (listener as MainActivity).viewPdf(evaluatedPdf)
            }

            override fun viewUploadedCopy(uploadedPdf: String?) {
                (listener as MainActivity).viewPdf(uploadedPdf)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        apiCallTypeWise()
    }

    private fun apiCallTypeWise() {
        when (type) {
            "pt-test" -> ptTestApiCall(packageId, programId)
            "mains-test" -> mainsTestApiCall(packageId, programId)
            "e-books" -> eBooksApiCall(packageId, programId)
            "video" -> videoListApiCall(packageId, type, programId)
            "Schedule" -> setScheduleAdapter()
            "Other" -> setCommentsAdapter()
            "Upcoming Batches" -> setViewAllBatchesAdapter()
            "My Downloads" -> videoListApiCall(packageId, type, programId)
        }
    }

    private fun setViewAllBatchesAdapter() {
        viewAllBatchesListAdapter = context?.let {
            ViewAllBatchesListAdapter(
                it,
                CourseDetailsFragment.courseResponse.courseInfo!!.batchesList,
                enrollContext,
                brochureClickContext
            )
        }
        binding.rvMaterial.adapter = viewAllBatchesListAdapter
    }

    private fun ptTestApiCall(packageId: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["package_id"] = packageId
        request["program_id"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.ptTestList(request)
            val response: AllTestResponse = result.body()!!
            if (response.status) {
                val bool = funDataNotFound(
                    response.programList.size,
                    response.ptTest.size,
                    response.programList
                )
                if (bool) {
                    loadingDialog.dismiss()
                    ptTestListAdapter = context?.let {
                        PtTestListAdapter(
                            it,
                            response.ptTest,
                            coroutineScope,
                            apiService,
                            loadingDialog
                        )
                    }
                    binding.rvMaterial.adapter = ptTestListAdapter
                }
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun mainsTestApiCall(packageId: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["package_id"] = packageId
        request["program_id"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.mainsTestList(request)
            val response: AllTestResponse = result.body()!!
            if (response.status) {
                val bool = funDataNotFound(
                    response.programList.size,
                    response.mainsTest.size,
                    response.programList
                )
                if (bool) {
                    Log.d("***** CourseTestFragment >>>", "" + response.mainsTest)
                    mainTestListAdapter =
                        context?.let {
                            MainTestListAdapter(
                                it,
                                response.mainsTest,
                                downloadBrochureInterface,
                                uploadAnswerInterface
                            )
                        }
                    binding.rvMaterial.adapter = mainTestListAdapter
                }
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }


    }

    private fun funDataNotFound(
        programListSize: Int,
        testListSize: Int,
        programList: ArrayList<ProgramList>
    ): Boolean {
        var bool = true
        if (programListSize == 0 && testListSize == 0) {
            binding.tvDataNotFound.visibility = View.VISIBLE
            binding.scrollView.visibility = View.GONE
            bool = false
        }
        if (programListSize > 0) {
            programListAdapter =
                context?.let { ProgramListAdapter(it, programList, type, clickListener!!) }
            binding.rvMaterial.adapter = programListAdapter
            bool = false
        }
        return bool
    }

    private fun eBooksApiCall(packageId: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["package_id"] = packageId
        request["program_id"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            try {
                val result = apiService.eBookList(request)
                val response: AllTestResponse = result.body()!!
                if (response.status) {
                    val bool = funDataNotFound(
                        response.programList.size,
                        response.ebookList.size,
                        response.programList
                    )
                    if (bool) {
                        eBooksListAdapter =
                            context?.let { EBooksListAdapter(it, response.ebookList) }
                        binding.rvMaterial.adapter = eBooksListAdapter
                    }
                } else {
                    Toast.makeText(
                        context,
                        response.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            loadingDialog.dismiss()
        }


    }

    private fun addToCartApiCall(batchesData: BatchesList, position: Int) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = batchesData.packageId!!
        request["mode"] = batchesData.classMode!!
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.addToCart(request)
            val response: CartItemResponse = result.body()!!
            if (response.status) {
                loadingDialog.dismiss()
                bottomSheetCourseType!!.dismiss()
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )

                    .show()
                val intent = Intent(context, OrderSummaryActivity::class.java)
                if (position == 1) {
                    intent.putExtra(
                        "amount",
                        "" + (batchesData.onlineAmount)
                    )
                    intent.putExtra("gst", batchesData.onlineGST)
                } else
                    intent.putExtra(
                        "amount",
                        "" + (batchesData.offlineAmount)
                    )
                intent.putExtra("gst", batchesData.offlineGST)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun videoListApiCall(packageId: String, type: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["package_id"] = packageId
        request["program_id"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.videoList(request)
            val response: AllTestResponse = result.body()!!
            if (response.status) {
                val bool = funDataNotFound(
                    response.programList.size,
                    response.videoList.size,
                    response.programList
                )
                if (bool) {
                    videosListAdapter = context?.let {
                        VideosListAdapter(
                            requireContext(), response.videoList,
                            type, response, packageId
                        )
                    }
                    binding.rvMaterial.adapter = videosListAdapter
                }
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun setScheduleAdapter() {
        materialTopList = getScheduleList();
        scheduleListAdapter = context?.let { ScheduleListAdapter(it, materialTopList) }
        binding.rvMaterial.adapter = scheduleListAdapter

    }

    private fun setCommentsAdapter() {
        var materialTopList = ArrayList<MaterialResponse>();
        commentsListAdapter = context?.let { CommentsListAdapter(it, materialTopList) }
        binding.rvMaterial.adapter = commentsListAdapter
        commentsListAdapter
    }

    private fun getScheduleList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("06-12-2020", true))
        list.add(MaterialResponse("08-12-2020", true))
        list.add(MaterialResponse("09-12-2020", true))
        return list;
    }

    private fun getCommentsList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("Puneet Chawala", true))
        list.add(MaterialResponse("Abhishek Verma", true))
        list.add(MaterialResponse("Priya Adhlakha", true))
        list.add(MaterialResponse("Vikrant Rana", true))
        list.add(MaterialResponse("Puneet Chawala", true))
        list.add(MaterialResponse("Abhishek Verma", true))
        list.add(MaterialResponse("Priya Adhlakha", true))
        list.add(MaterialResponse("Vikrant Rana", true))
        return list;
    }

    private fun getViewAllBatchesList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("Evening Batch-3", true))
        list.add(MaterialResponse("Evening Batch-2", true))
        list.add(MaterialResponse("Evening Batch-3", true))
        list.add(MaterialResponse("Evening Batch-2", true))
        list.add(MaterialResponse("Evening Batch-3", true))
        list.add(MaterialResponse("Evening Batch-2", true))
        list.add(MaterialResponse("Evening Batch-3", true))
        list.add(MaterialResponse("Evening Batch-2", true))
        return list;
    }

    override fun onClick(programId: String, type: String) {
        this.programId = programId
        apiCallTypeWise()
    }

    override fun downLoadFile(url: String, title: String, position: Int) {
        if (url.isNotEmpty()) {
            val extension = url?.substring(url.lastIndexOf("."))
            val fileName = title + "pdf" + System.currentTimeMillis() + "." + extension
            CHANNEL_DESC = title
            val requestID = System.currentTimeMillis().toInt()
            NOTIFICATION_ID = requestID
            Helpers.startDownloadingFile(null,
                requireActivity(),
                url,
                fileName,
                "PDF",
                workManager,
                requireActivity()
            )
        } else
            Toast.makeText(requireActivity(), "URL not found!", Toast.LENGTH_SHORT).show()
    }


    override fun onEnrollClick(position: Int) {
        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
        bottomSheetCourseType =
            CourseTypeBottomSheet(
                CourseDetailsFragment.courseResponse.courseInfo!!.batchesList[position],
                interfaceCourseTypeContex
            ).apply {
                show(SingletonClass.instance.containerFragmentManager!!, CourseTypeBottomSheet.TAG)
            }
        /* bottomSheetCourseType =
             CourseTypeBottomSheet(

                 CourseDetailsFragment.courseResponse.courseInfo!!.batchesList[position],
                 interfaceCourseTypeContex
             ).apply {
                 show(SingletonClass.instance.supportFragmentManager!!, CourseTypeBottomSheet.TAG)
             }*/
    }

    override fun onClickCourseType(position: Int, batchesData: BatchesList) {
        selectedPosCourseType = position
        addToCartApiCall(batchesData, position)

    }

    override fun onBrochureClick(position: Int) {
        downLoadFile(
            CourseDetailsFragment.courseResponse.courseInfo!!.courseDetail!!.packageBrochureUrl!!,
            CourseDetailsFragment.courseResponse.courseInfo!!.courseDetail!!.courseTitle!!,
            0
        )
    }

    fun notifyAdapter() {
       mainTestListAdapter?.notifyDataSetChanged()
    }
}
