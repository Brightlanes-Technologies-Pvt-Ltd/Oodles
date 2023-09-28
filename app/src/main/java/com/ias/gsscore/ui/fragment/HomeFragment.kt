package com.ias.gsscore.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.databinding.FragmentHomeBinding
import com.ias.gsscore.R
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.activity.WebViewActivity
import com.ias.gsscore.ui.bottomsheet.NoInternetBottomSheetFragment
import com.ias.gsscore.ui.viewmodel.HomeFragmentViewModel
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeFragmentViewModel
    var bottomSheet= NoInternetBottomSheetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("***** HomeFragment", "onCreate >>>>>")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("***** HomeFragment", "onCreateView >>>>>")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //Clear View Model
        viewModelStore.clear()
        viewModel = ViewModelProvider(this,ActivityViewModelFactory(binding, SingletonClass.instance))[HomeFragmentViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = requireActivity()
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(requireActivity())
        viewModel.sliderPagerSet()
        if(Helpers.isNetworkAvailable(requireActivity())) {
            viewModel.fcm()
            viewModel.homeApi()
            viewModel.deliveryItemListApiCall()
        }else{
            bottomSheet!!.show(requireActivity().supportFragmentManager, "ModalBottomSheet")
        }
        Preferences.getInstance(context).frTitle=""

        binding.interviews.setOnClickListener {
            startActivity(Intent(requireActivity(), WebViewActivity::class.java).putExtra("url", "https://iasscore.in/toppers-interview"))
        }

        binding.copies.setOnClickListener {
            startActivity(Intent(requireActivity(), WebViewActivity::class.java).putExtra("url", "https://iasscore.in/toppers-copy"))
        }

        binding.results.setOnClickListener {
            startActivity(Intent(requireActivity(), WebViewActivity::class.java).putExtra("url", "https://iasscore.in/our-results"))
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}