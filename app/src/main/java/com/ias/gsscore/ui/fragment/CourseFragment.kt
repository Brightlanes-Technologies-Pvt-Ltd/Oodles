package com.ias.gsscore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.ias.gsscore.R
import com.ias.gsscore.DownloadBrochureInterface
import com.ias.gsscore.databinding.FragmentCourseBinding
import com.ias.gsscore.downloadfileswithworkmanager.*
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.viewmodel.CourseFragmentViewModel
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class CourseFragment : Fragment(), DownloadBrochureInterface {
    lateinit var binding: FragmentCourseBinding
    lateinit var viewModel: CourseFragmentViewModel
    lateinit var workManager: WorkManager
    var courseType:String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course, container, false)
        viewModel = ViewModelProvider(this,ActivityViewModelFactory(binding,SingletonClass.instance))[CourseFragmentViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = requireActivity()
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(requireActivity())
        SingletonClass.instance.parentList.clear()
        courseType = arguments?.getString("id").toString()
        if(courseType=="null"){
            courseType=""
        }
        workManager = WorkManager.getInstance(requireActivity())
        viewModel.downloadBrochureInterface = this
        return binding.root
    }

    override fun onResume() {
        viewModel.courseListApi(courseType)
        super.onResume()
    }



    override fun downLoadFile(url: String, title: String,position:Int) {
        if (url.isNotEmpty()){
            val extension = url?.substring(url.lastIndexOf("."))
            val fileName = title+"pdf" + System.currentTimeMillis() +"."+ extension
            CHANNEL_DESC = title
            val requestID = System.currentTimeMillis().toInt()
            NOTIFICATION_ID = requestID
            Helpers.startDownloadingFile(null, requireActivity(),url,fileName,"PDF",workManager,requireActivity())
        }else
            Toast.makeText(requireActivity(),"URL not found!",Toast.LENGTH_SHORT).show()
    }
}