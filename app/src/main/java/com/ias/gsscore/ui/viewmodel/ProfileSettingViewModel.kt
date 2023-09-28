package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityProfileSettingBinding
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.ui.activity.ChatActivity
import com.ias.gsscore.ui.activity.PersonalProfileActivity

class ProfileSettingViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityProfileSettingBinding

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var isOnline: ObservableInt = ObservableInt(0)

    fun clickOnlineLayout() {
        if (isOnline.get() == 0)
            isOnline.set(1)
        else
            isOnline.set(0)
    }

    fun clickPersonalProfile() {
        context.startActivity(Intent(context, PersonalProfileActivity::class.java))
    }

    fun clickPrivacyChat() {
        val intent: Intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("position", 0)
        context.startActivity(intent)
    }
    fun clickTagChat() {
        val intent: Intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("position", 1)
        context.startActivity(intent)
    }
    fun clickAssigned() {
        val intent: Intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("position", 2)
        context.startActivity(intent)
    }
    fun clickEvents() {
        val intent: Intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("position", 3)
        context.startActivity(intent)
    }

    private fun dataList(bankList: ArrayList<MaterialResponse>): List<MaterialResponse> {
        bankList.add(MaterialResponse("Report & magazines", false))
        return bankList
    }
}