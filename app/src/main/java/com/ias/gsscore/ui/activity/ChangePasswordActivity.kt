package com.ias.gsscore.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityChangePasswordBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.LoginResponse
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    lateinit var loadingDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        loadingDialog = RetrofitHelper.loadingDialog(this)
        binding.etMobile.setText(Preferences.getInstance(this).userMobile)
        binding.update.setOnClickListener{
            if(binding.etPassword.text.toString().isNotEmpty()&&binding.etConfirmPassword.text.toString().isNotEmpty()){
                if(binding.etPassword.text.toString()==binding.etConfirmPassword.text.toString()){
                    updateNewPassword()
                    }
            }


        }
    }

    private fun updateNewPassword() {
        var request: HashMap<String, String> = HashMap()
        request["email"] = binding.etMobile.text.toString()
        request["password"] = binding.etPassword.text.toString()
        request["confirm_password"] = binding.etConfirmPassword.text.toString()
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.updateNewPassword(request)
            val response: LoginResponse = result.body()!!
            if (response == null) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            loadingDialog.dismiss()
            Toast.makeText(this@ChangePasswordActivity, response!!.message, Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }
}