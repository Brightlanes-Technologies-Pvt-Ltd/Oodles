package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkManager
import coil.load
import com.ias.gsscore.network.response.courses.BatchesList
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentCourseDetailsBinding
import com.ias.gsscore.downloadfileswithworkmanager.CHANNEL_DESC
import com.ias.gsscore.downloadfileswithworkmanager.NOTIFICATION_ID
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.cart.CartItemResponse
import com.ias.gsscore.network.response.courses.Category
import com.ias.gsscore.network.response.courses.CourseDetail
import com.ias.gsscore.network.response.courses.CourseDetailsResponse
import com.ias.gsscore.network.response.courses.CourseInfos
import com.ias.gsscore.ui.activity.ContainerActivity
import com.ias.gsscore.ui.activity.OrderSummaryActivity
import com.ias.gsscore.ui.adapter.VideoDetailsPagerAdapter
import com.ias.gsscore.ui.bottomsheet.BatchListBottomSheet
import com.ias.gsscore.ui.bottomsheet.CourseTypeBottomSheet
import com.ias.gsscore.ui.viewmodel.MainViewModel
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class CourseDetailsFragment() : Fragment(), BatchListBottomSheet.BottomSheetClick,
    CourseTypeBottomSheet.BottomSheetCourseTypeClick {
    lateinit var binding: FragmentCourseDetailsBinding
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)

    private lateinit var courseInfoList: ArrayList<CourseInfos>
    private var bottomSheetBatchList: BatchListBottomSheet? = null
    private var bottomSheetCourseType: CourseTypeBottomSheet? = null
    private var courseId = ""
    private lateinit var interfaceContex: BatchListBottomSheet.BottomSheetClick
    private lateinit var interfaceCourseTypeContex: CourseTypeBottomSheet.BottomSheetCourseTypeClick
    private var selectedPosCourseType = -1
    private var stateList: ArrayList<Category> = arrayListOf()
    lateinit var workManager: WorkManager

    companion object{
         var courseResponse = CourseDetailsResponse()
    }

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_course_details, container, false)
        // val view: View = inflater.inflate(R.layout.fragment_pt_test, container, false)
        loadingDialog = RetrofitHelper.loadingDialog(requireActivity())
        courseId = arguments?.getString("id").toString()
        val title = arguments?.getString("title").toString()
        workManager = WorkManager.getInstance(requireActivity())
        interfaceContex = this
        interfaceCourseTypeContex = this
        initialData()
        MainViewModel.setHeaderTitle(1, title)
        return binding.root;

    }

    override fun onDestroy() {
        super.onDestroy()
        MainViewModel.setHeaderTitle(0, "")
    }

    private fun initialData() {
        courseDetailsApi(courseId)
        binding.playVideo.setOnClickListener {
            watchYoutubeVideo()
        }
        binding.btAllBatches.setOnClickListener {
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra("whereFrom", "viewAllBatches")
            startActivity(intent)
           // clickToBottomSheet()
        }
        binding.btBrochure.setOnClickListener {
            /*   val intent = Intent(context, WebViewActivity::class.java)
               intent.putExtra("title", courseResponse.courseInfo!!.courseDetail!!.courseTitle)
               intent.putExtra("url", courseResponse.courseInfo!!.courseDetail!!.packageBrochureUrl)
               requireContext().startActivity(intent)*/
            // Helpers.downloadPdf(requireContext(),courseResponse.courseInfo!!.courseDetail!!.packageBrochureUrl, courseResponse.courseInfo!!.courseDetail!!.courseTitle)
            downLoadFile(
                courseResponse.courseInfo!!.courseDetail!!.packageBrochureUrl!!,
                courseResponse.courseInfo!!.courseDetail!!.courseTitle!!,
                0
            )
        }
        binding.enrollNow.setOnClickListener {
            clickToBottomSheet()
        }

        binding.btCall.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            if (courseResponse.courseInfo!!.courseDetail!!.marketingNumber != "") {
                dialIntent.data =
                    Uri.parse("tel:" + courseResponse.courseInfo!!.courseDetail!!.marketingNumber)
                startActivity(dialIntent)
            } else {
                Toast.makeText(context, "No marketing number present", Toast.LENGTH_LONG).show()
            }

        }
        binding.btEnquiry.setOnClickListener {
            dialogEnquiry()
        }
    }

    private fun dialogEnquiry() {
        try {
            val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
            dialog.window!!.requestFeature(1)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.dialog_enquiry_now)
            dialog.setCancelable(true)
            val yes = dialog.findViewById<LinearLayout>(R.id.yes)
            val no = dialog.findViewById<LinearLayout>(R.id.no)
            val etName = dialog.findViewById<EditText>(R.id.etName)
            val etEmail = dialog.findViewById<EditText>(R.id.etEmail)
            val etMobile = dialog.findViewById<EditText>(R.id.etMobile)
            val etState = dialog.findViewById<Spinner>(R.id.etState)
            val message = dialog.findViewById<EditText>(R.id.etMessage)
            etName.setText(Preferences.getInstance(requireActivity()).userName)
            etEmail.setText(Preferences.getInstance(requireActivity()).userEmail)
            etMobile.setText(Preferences.getInstance(requireActivity()).userMobile)

            var spinnerList: Array<String?> = arrayOfNulls(stateList.size + 1)
            spinnerList[0] = "Select State"
            for (i in stateList.indices) {
                spinnerList[i + 1] = stateList[i].stateName
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item, spinnerList
            )
            etState.adapter = adapter
            var stateId = ""
            etState.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    (etState.selectedView as TextView).setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    stateId = if (position == 0)
                        ""
                    else
                        stateList[position].id!!
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            no.setOnClickListener { dialog.dismiss() }
            yes.setOnClickListener {
                if (stateId == "") {
                    Toast.makeText(
                        context,
                        "Please select state",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                if (message.text.toString() == "") {
                    Toast.makeText(
                        context,
                        "Please enter message",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                enquiryApiCall(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etMobile.text.toString(),
                    stateId,
                    message.text.toString(),
                    dialog
                )
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun enquiryApiCall(
        etName: String,
        etEmail: String,
        etMobile: String,
        etState: String,
        etMessage: String,
        dialog: Dialog
    ) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(requireActivity()).userId
        request["identifier"] = courseId
        request["name"] = etName
        request["email"] = etEmail
        request["mobile"] = etMobile
        request["state"] = etState
        request["message"] = etMessage
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.enquiryNow(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: OodlesApiResponse = result.body()!!
            dialog.dismiss()
            Toast.makeText(
                requireActivity(),
                response.message,
                Toast.LENGTH_SHORT
            )
                .show()
            loadingDialog.dismiss()
        }
    }

    private fun courseDetailsApi(courseId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(requireActivity()).userId
        request["course_id"] = courseId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.courseDetails(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: CourseDetailsResponse = result.body()!!
            courseResponse = response
            courseInfoList = courseResponse.courseInfo!!.courseInfos
            stateList = courseResponse.stateList
            if (courseResponse.status) {
                setCourseDetails(courseResponse.courseInfo!!.courseDetail!!)
                setTabLayout()
            } else {
                Toast.makeText(
                    requireActivity(),
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCourseDetails(courseDetail: CourseDetail) {
        binding.ivImage.load(courseDetail!!.courseImg)
        if (courseDetail.courseYoutubeId.equals("")) {
            binding.btPlay.visibility = View.GONE
            //https://www.youtube.com/watch?v=n261iHgD1rs
        }
        binding.tvCourseTitle.text = courseDetail!!.courseTitle
//
        Helpers.setWebViewText(binding.tvCourseDesc, courseDetail.courseDesc!!)
        binding.tvBatchStartDate.text = "Start From ${courseDetail.batchStartDate}"
        binding.tvBatchTime.text = courseDetail.batchTime
        if (courseDetail.onlineGST!!.toFloat() > 0) {
            binding.tvOnlineAmount.text =
                "₹${courseDetail.packageOnineAmount!!} (+GST)"
            binding.onlineGst.visibility = View.GONE
        } else {
            binding.tvOnlineAmount.text = "₹${courseDetail.packageOnineAmount!!} "
            binding.onlineGst.visibility = View.VISIBLE
        }
        if (courseDetail.offlineGST!!.toFloat() > 0) {
            binding.tvOfflineAmount.text =
                "₹${courseDetail.packageOfflineAmount!!} (+GST)"
            binding.offlineGst.visibility = View.GONE
        } else {
            binding.tvOfflineAmount.text = "₹${courseDetail.packageOfflineAmount!!} "
            binding.offlineGst.visibility = View.VISIBLE
        }
        binding.tvBatchStartDateBottom.text = "Start From ${courseDetail.batchStartDate}"
        binding.tvBatchTimeBottom.text = courseDetail.batchTime

        when {
            courseDetail.classMode.equals("1") -> {
                binding.layoutOnline.visibility = View.VISIBLE
                binding.layoutOffline.visibility = View.GONE
            }
            courseDetail.classMode.equals("2") -> {
                binding.layoutOnline.visibility = View.GONE
                binding.layoutOffline.visibility = View.VISIBLE
            }
            else -> {
                binding.layoutOnline.visibility = View.VISIBLE
                binding.layoutOffline.visibility = View.VISIBLE
            }
        }
    }

    private fun watchYoutubeVideo() {
        val id = courseResponse.courseInfo!!.courseDetail!!.courseYoutubeId
        if (!id.equals("")) {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id")
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }
    }

    private fun setTabLayout() {
        var viewPagerAdapter =
            VideoDetailsPagerAdapter(
                childFragmentManager,
                lifecycle,
                requireContext(),
                "videoDetailsNew",
                courseInfoList.size,
                courseInfoList,
                null
            )
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.tabRippleColor = null
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            Log.e("TAG", "setTabLayout: $position")
            tab.text = courseInfoList[position].title
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        })
    }

    private fun clickToBottomSheet() {
        bottomSheetBatchList =
            BatchListBottomSheet(
                requireActivity(),
                courseResponse.courseInfo!!.batchesList,
                interfaceContex
            ).apply {
                show(SingletonClass.instance.supportFragmentManager!!, BatchListBottomSheet.TAG)
            }
    }

    override fun onCLick(position: Int) {
        bottomSheetBatchList!!.dismiss()
        bottomSheetCourseType =
            CourseTypeBottomSheet(
                courseResponse.courseInfo!!.batchesList[position],
                interfaceCourseTypeContex
            ).apply {
                show(SingletonClass.instance.supportFragmentManager!!, CourseTypeBottomSheet.TAG)
            }
    }

    override fun onClickCourseType(position: Int, batchesData: BatchesList) {
        selectedPosCourseType = position
        addToCartApiCall(batchesData, position)

    }

    private fun addToCartApiCall(batchesData: BatchesList, position: Int) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = batchesData.packageId!!
        request["mode"] = batchesData.classMode!!
        coroutineScope.launch {
            loadingDialog.show()

            val result = apiService.addToCart(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
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
                if (position == 1){
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
                intent.putExtra("flag", "CourseDetail")
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

fun downLoadFile(url: String, title: String, position: Int) {
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


}
