package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentHomeBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.network.response.home.*
import com.ias.gsscore.ui.adapter.DotIndicatorPager2Adapter
import com.ias.gsscore.ui.adapter.FreeInitiativesListAdapter
import com.ias.gsscore.ui.adapter.LatestCourseListAdapter
import com.ias.gsscore.ui.adapter.StudyMaterialsListAdapter
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.utils.ZoomOutPageTransformer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as FragmentHomeBinding
    var page = 0

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var freeInitiative: ObservableInt = ObservableInt(0)
    var adapterSliderPager = DotIndicatorPager2Adapter()
    var adapterLatestCourse = LatestCourseListAdapter()
    var adapterFreeResources = FreeInitiativesListAdapter()
    var adapterCurrentAffairs = FreeInitiativesListAdapter()
    var adapterStudyMaterials = StudyMaterialsListAdapter()
    lateinit var loadingDialog: AlertDialog
    val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var sliderList: ArrayList<SliderImages> = arrayListOf()
    private var latestCourseList: ArrayList<LatestCourses> = arrayListOf()
    private var freeResourcesList: ArrayList<FreeResourcesCategories> = arrayListOf()
    private var currentAffairsList: ArrayList<FreeResourcesCategories> = arrayListOf()
    private var studyMaterialsList: ArrayList<StudyMaterialList> = arrayListOf()
    lateinit var cartResponse: CartItemListResponse

    fun clickFreeResources() {
        freeInitiative.set(0)
    }

    fun viewAll() {
        SingletonClass.instance.getCustomNavController().navigate(R.id.studyNotesFragment)
    }

    fun viewAllCourse() {
        SingletonClass.instance.getCustomNavController().navigate(R.id.courseFragment)
    }

    fun clickCurrentAffairs() {
        freeInitiative.set(1)
    }

    @SuppressLint("SetTextI18n")
    fun homeApi() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.homeDetails(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: HomeResponse = result.body()!!
            if (response.status) {
                //Slider
                sliderList = response.sliderImages
                adapterSliderPager!!.update(sliderList)
                // Latest Course
                latestCourseList = response.latestCourses
                adapterLatestCourse.update(latestCourseList, latestCourseList.size, context)
                // Free Resources
                freeResourcesList = response.freeResourcesCategories
                adapterFreeResources.update(
                    freeResourcesList,
                    "FreeResources",
                    freeResourcesList.size,
                    context
                )
                //Current Affairs
                currentAffairsList = response.currentAffairsCategories
                adapterCurrentAffairs.update(
                    currentAffairsList,
                    "CurrentAffairs",
                    currentAffairsList.size,
                    context
                )
                //Study Materials
                adapterStudyMaterials.context = context
                studyMaterialsList = response.studyMaterialList
                adapterStudyMaterials.update(studyMaterialsList, studyMaterialsList.size)
                funGSScoreCourse(response.courseCount)
                /*  if (latestCourseList.size > 0) {
                      if (latestCourseList.size >= 4 )
                          adapterLatestCourse.update(latestCourseList,4)
                      else
                          adapterLatestCourse.update(latestCourseList,latestCourseList.size)
                  }
                  if (freeResourcesList.size > 0) {
                      if (freeResourcesList.size >= 8 )
                          adapterFreeResources.update(freeResourcesList, "FreeResources",8)
                      else
                          adapterFreeResources.update(freeResourcesList, "FreeResources",freeResourcesList.size)
                  }
                  if (currentAffairsList.size > 0) {
                      if (currentAffairsList.size >= 8 )
                          adapterCurrentAffairs.update(currentAffairsList, "CurrentAffairs",8)
                      else
                          adapterCurrentAffairs.update(currentAffairsList, "CurrentAffairs",currentAffairsList.size)
                  }
                  if (studyMaterialsList.size > 0) {
                      if (studyMaterialsList.size >=3)
                          adapterStudyMaterials.update(studyMaterialsList, 3)
                      else
                          adapterStudyMaterials.update(studyMaterialsList, studyMaterialsList.size)
                  }*/
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

    private fun funGSScoreCourse(courseCount: CourseCount?) {
        binding.gsClasses.text = courseCount!!.gsClasses.toString()
        binding.optionalClasses.text = courseCount!!.optionalClasses.toString()
        binding.testSeries.text = courseCount!!.testSeries.toString()
    }

    fun sliderPagerSet() {
        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page = position
            }

        })
        adapterSliderPager = DotIndicatorPager2Adapter()
        binding.viewPager2.adapter = adapterSliderPager
        binding.dotsIndicator.setViewPager2(binding.viewPager2)
    }

    fun funCourseType1() {
        val bundle = bundleOf("id" to "1")
        SingletonClass.instance.getCustomNavController()
            .navigate(R.id.courseFragment, bundle)
    }

    fun funCourseType2() {
        val bundle = bundleOf("id" to "2")
        SingletonClass.instance.getCustomNavController()
            .navigate(R.id.courseFragment, bundle)
    }

    fun funCourseType3() {
        val bundle = bundleOf("id" to "3")
        SingletonClass.instance.getCustomNavController()
            .navigate(R.id.courseFragment, bundle)
    }

     fun fcm() {
        var request: HashMap<String, String> = HashMap()
        request["device_id"] = Preferences.getInstance(context).fcmToken
        request["user_id"] = Preferences.getInstance(context).userId

        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.fcm(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: OodlesApiResponse = result.body()!!
            if (response.status) {

            }

        }
        loadingDialog.dismiss()

    }

    fun deliveryItemListApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            loadingDialog.show()

            val result = apiService.cartItemList(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            cartResponse = result.body()!!
            if (cartResponse.status) {
                SingletonClass.instance.cartCountLayout!!.visibility= View.VISIBLE
                SingletonClass.instance.cartCount!!.text=cartResponse.cartItems!!.totalItems.toString()



            } else {
                Toast.makeText(
                    context,
                    cartResponse.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }
}
