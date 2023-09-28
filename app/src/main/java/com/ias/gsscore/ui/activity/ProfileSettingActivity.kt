package com.ias.gsscore.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.databinding.ActivityProfileSettingBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.myaccount.ProfileResponse
import com.ias.gsscore.ui.viewmodel.HeaderBackViewModel
import com.ias.gsscore.ui.viewmodel.ProfileSettingViewModel
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileSettingActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileSettingBinding
    lateinit var viewModel: ProfileSettingViewModel
    lateinit var headerBackViewModel: HeaderBackViewModel
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[ProfileSettingViewModel::class.java]
        headerBackViewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[HeaderBackViewModel::class.java]
        setContentView(binding.root)
        binding.viewmodel = viewModel
        loadingDialog = RetrofitHelper.loadingDialog(this)
        binding.headerBack.backHeaderViewModel = headerBackViewModel
        viewModel.context = this
        headerBackViewModel.context = this
        binding.headerBack.tvTitle.text = "Profile Setting"
        getProfile()

        binding.changePassword.setOnClickListener{
            startActivity(Intent(this,ChangePasswordActivity::class.java))
        }
    }

    private fun getProfile() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(this).userId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.userProfile(request)
            val response: ProfileResponse = result.body()!!
            if (response.status) {
                binding.tvUserName.text = Preferences.getInstance(applicationContext).userName
                binding.tvRollNo.text = response.user_profile.contact
                if(response.user_profile.user_type!="1"){
                    binding.layoutMessaging.visibility= View.GONE
                    binding.layoutPrivacyChat.visibility= View.GONE
                    binding.layoutSessions.visibility= View.GONE
                    binding.layoutEvents.visibility= View.GONE
                    binding.layoutAssignations.visibility= View.GONE
                    binding.layoutAssigned.visibility= View.GONE
                    binding.online.visibility= View.GONE
                    binding.layoutOnlineHide.visibility= View.GONE

                }else{

                }
            }
            else {
                Toast.makeText(
                    applicationContext,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }
}