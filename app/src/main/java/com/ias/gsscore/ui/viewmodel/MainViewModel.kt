package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.view.GravityCompat
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityMainBinding
import com.ias.gsscore.ui.activity.CartItemListActivity
import com.ias.gsscore.ui.activity.LoginActivity
import com.ias.gsscore.ui.activity.NotificationActivity
import com.ias.gsscore.ui.activity.ProfileSettingActivity
import com.ias.gsscore.utils.Preferences

class MainViewModel(binding: ViewDataBinding) : ViewModel() {
    var isSelected: ObservableInt = ObservableInt(0)
    val binding = binding as ActivityMainBinding

    companion object{
        var isTitleShow: ObservableInt = ObservableInt(0)
        var headerTitle : String = ""
        fun setHeaderTitle(value:Int,title:String){
            isTitleShow.set(value)
            headerTitle = title
        }
    }

    fun funBackPress(){
        setHeaderTitle(0,"")
        (context as Activity).onBackPressed()
    }

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    fun setDrawer() {
        binding.tvHeaderTitle.text = ""
        binding.tvUserName.text = Preferences.getInstance(context).userName
      /*  binding.tvRollNo.text = "Roll No.: "*/
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun settingLayout() {
        context.startActivity(Intent(context, ProfileSettingActivity::class.java))
    }

    fun clickCartButton() {
        val intent = Intent(context, CartItemListActivity::class.java)
        intent.putExtra("whereFrom","cart")
        context.startActivity(intent)
    }

    fun clickNotification() {
        val intent = Intent(context, NotificationActivity::class.java)
        intent.putExtra("whereFrom","cart")
        context.startActivity(intent)
    }

    fun clickMyWishList() {
        val intent = Intent(context, CartItemListActivity::class.java)
        intent.putExtra("whereFrom","wishlist")
        context.startActivity(intent)
    }

    fun clickLogout() {
        Preferences.getInstance(context).isLogin = false
        Preferences.getInstance(context).userId = ""
        Preferences.getInstance(context).userType = ""
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }

    fun funHeaderManage(bool:Boolean,whereFrom:String){
        if (bool){

        }else{

        }
    }


}