package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentMaterialBinding
import com.ias.gsscore.network.response.myaccount.PackageHasResponse
import com.ias.gsscore.network.response.myaccount.PackageList
import com.ias.gsscore.ui.activity.MentorBoxActivity
import com.ias.gsscore.ui.adapter.MaterialTopListAdapter
import com.ias.gsscore.ui.viewmodel.MainViewModel
import com.ias.gsscore.utils.SingletonClass

class MyAccountTopListFragment : Fragment(), MaterialTopListAdapter.ClickListener {
    lateinit var binding: FragmentMaterialBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    var materialTopList = ArrayList<PackageHasResponse>();
    private var materialTopListAdapter: MaterialTopListAdapter? = null
    private var clickListener: MaterialTopListAdapter.ClickListener? = null
    private var whereFrom = "MyAccount";
    private var packageData:PackageList?=null
    private var courseType = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        val gson = Gson()
        val data = arguments?.getString("data").toString()
        packageData = gson.fromJson(data,PackageList::class.java)
        MainViewModel.setHeaderTitle(1,packageData!!.title!!)
        initialData()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        MainViewModel.setHeaderTitle(0,"")
    }

    private fun initialData() {
        clickListener = this
        setTopAdapter()
       /* if (whereFrom=="MyAccount"){
            setTopAdapter()
            addFragment(CourseTestFragment(requireContext(), materialTopList[0].nameame, false), requireActivity())
        }else{
            addFragment(CourseTestFragment(requireContext(), "My Downloads", false), requireActivity())
        }*/
    }

    private fun setTopAdapter() {
        materialTopList = getTopList();
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMaterialTop.layoutManager = linearLayoutManager
        materialTopListAdapter = MaterialTopListAdapter(clickListener, materialTopList)
        binding.rvMaterialTop.adapter = materialTopListAdapter

        addFragment(CourseTestFragment(requireContext(), materialTopList[courseType].nameame, false,
            packageData!!.id!! /*"1099"*/), requireActivity(), CourseTestFragment::javaClass.name)
    }

    private fun getTopList(): java.util.ArrayList<PackageHasResponse> {
        var list = ArrayList<PackageHasResponse>()
        val str = packageData!!.packageHas!!.split(",")
        for (i in str.indices){
            if (packageData!!.courseType=="1" && str[i]=="video"){
                courseType = i
                list.add(PackageHasResponse(i+1,str[i], true))
            } else if (packageData!!.courseType=="2" && str[i]=="pt-test"){
                courseType = i
                list.add(PackageHasResponse(i+1,str[i], true))
            }
            else if (packageData!!.courseType=="3" && str[i]=="mains-test"){
                courseType = i
                list.add(PackageHasResponse(i+1,str[i], true))
            } else if (packageData!!.courseType=="5" && str[i]=="video"){
                courseType = i
                list.add(PackageHasResponse(i+1,str[i], true))
            }
            else
            list.add(PackageHasResponse(i+1,str[i], false))
        }

       /* list.add(PackageHasResponse(1,"pt-test", true))
        list.add(PackageHasResponse(2,"video", false))
        list.add(PackageHasResponse(3,"mains-test", false))
        list.add(PackageHasResponse(4,"e-books", false))
        list.add(PackageHasResponse(5,"Schedule", false))
        list.add(PackageHasResponse(6,"Mentor Box", false))*/

        return list;
    }


    companion object {
        fun addFragment(fragment: Fragment?, requireActivity: FragmentActivity, fragmentTag : String) {
            SingletonClass.instance.setSupportManager(requireActivity.supportFragmentManager)
            if (fragment == null) return
            requireActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.rootContainer, fragment, fragmentTag)
                .commitAllowingStateLoss()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(position: Int) {
        for ((index, value) in materialTopList.withIndex()) {
            materialTopList[index].bool = false;
        }
        materialTopList[position].bool = true;
        materialTopListAdapter!!.notifyDataSetChanged()
        if (materialTopList[position].nameame == "Mentor Box") {
            startActivity(Intent(requireContext(), MentorBoxActivity::class.java))
        } else {
            addFragment(CourseTestFragment(requireContext(), materialTopList[position].nameame, false,/*"1099"*/ packageData!!.id!!), requireActivity(), CourseTestFragment::javaClass.name)
        }
        /*  when (materialTopList[position].nameame) {
              "PT Test" -> addFragment(CourseTestFragment(this,materialTopList[position].nameame), this)
              "Main Tests" -> addFragment(CourseTestFragment(this,materialTopList[position].nameame), this)
              "E-Books" -> addFragment(CourseTestFragment(this,materialTopList[position].nameame), this)
              "Videos" -> addFragment(CourseTestFragment(this,materialTopList[position].nameame), this)
          }*/
    }

}