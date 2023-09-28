package com.ias.gsscore.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityLoginBinding
import com.ias.gsscore.network.request.LoginRegisterRequest
import com.ias.gsscore.network.response.LoginResponse
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.google.GoogleLoginResponse
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    lateinit var loadingDialog: AlertDialog
    private var login_with_otp = false;
    private var otp_layout_clicked = false;
    private var forgot_password = false;
    var RC_SIGN_IN = 1
    var g_name=""
    var g_email=""
    var g_id=""
    var user_id=""
    var gmail_click=false
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        loadingDialog = RetrofitHelper.loadingDialog(this)
        binding.tvForgotPassword.setOnClickListener {
            binding.loginOtp.text = getString(R.string.forget_password_text_click)
            binding.loginButton.text = "Get Code"
            binding.txtTitle.text = "Forget your password?"
            binding.txtTitle.setTextColor(resources.getColor(R.color.black))
            binding.txtSubTitle.text = "Enter email address to get the verification code"
            binding.tvMobileField.text = "Email/Mobile"
            binding.tvAccountType.text = "Email/Mobile"
            binding.etMobileRegister.hint = "Enter Email Address"
            forgot_password = true
            login_with_otp = true
            binding.loginEmailLayout.visibility = View.GONE
    //        binding.loginOtpLayout.visibility = View.VISIBLE
            binding.orLoginWith.visibility = View.GONE
            binding.btGoogleAccount.visibility = View.GONE

        }


        binding.tvAccountType.setOnClickListener {
            forgot_password = false
            binding.orLoginWith.visibility = View.VISIBLE
            binding.btGoogleAccount.visibility = View.VISIBLE
            binding.txtTitle.text = "Welcome"
            binding.txtTitle.setTextColor(resources.getColor(R.color.blue_text))
            binding.txtSubTitle.text = "Login to your Account"
            binding.tvMobileField.text = "Email/Mobile"
            binding.registerDetailsLayout.visibility = View.GONE
            binding.loginDetailsLayout.visibility = View.VISIBLE
            binding.txtLogin.setTextColor(Color.parseColor("#005DA8"))
            binding.txtRegister.setTextColor(Color.parseColor("#ACACAC"))
            binding.viewRegister.visibility = View.GONE
            binding.viewLogin.visibility = View.VISIBLE
            if (binding.tvAccountType.text.toString().contains("OTP")) {
                login_with_otp = true
                binding.loginEmailLayout.visibility = View.GONE
                binding.loginOtpLayout.visibility = View.VISIBLE
//                binding.loginOtp.text = "Click here to Login with Email/Mobile"
                binding.loginButton.text = "Login"
                binding.tvMobileField.text = "Mobile Number"
                binding.etMobileRegister.hint = "Enter Mobile Number"
                binding.tvAccountType.text = "Email/Mobile"
            } else {
                login_with_otp = false
                binding.tvAccountType.text = "Mobile/OTP"
                binding.loginEmailLayout.visibility = View.VISIBLE
                binding.loginOtpLayout.visibility = View.GONE
//                binding.loginOtp.text =
//                    "Click here to Login with &quot; One time password &quot; OTP"
                binding.loginButton.text = "Login"
            }

        }
        binding.tvAccountType.text = "Mobile/OTP"

        binding.login.setOnClickListener {
            binding.etFullName.isEnabled=true;
            binding.etMail.isEnabled=true;
            forgot_password = false
//            gmail_click=false
            binding.tvAccountType.text = "Email/Mobile"

            loginUiUpdate()
        }

        binding.register.setOnClickListener {
 //           gmail_click=false
            binding.etFullName.isEnabled=true;
            binding.etMail.isEnabled=true;
            forgot_password = false
            binding.orLoginWith.visibility=View.VISIBLE
            binding.btGoogleAccount.visibility=View.VISIBLE
            binding.otpView.setText("")
            otp_layout_clicked = false
            binding.loginLayout.visibility = View.VISIBLE
            binding.viewRegister.visibility = View.VISIBLE
            binding.viewLogin.visibility = View.GONE
            binding.loginEmailLayout.visibility = View.GONE
            binding.loginOtpLayout.visibility = View.VISIBLE
            binding.txtLogin.setTextColor(Color.parseColor("#ACACAC"))
            binding.txtRegister.setTextColor(Color.parseColor("#005DA8"))
            binding.loginOtp.visibility = View.GONE
            binding.txtTitle.text = "Create your Account"
            binding.tvMobileField.text = "Email/Mobile"
            binding.tvAccountType.text = "Email/Mobile"
            binding.txtSubTitle.visibility = View.GONE
            binding.loginButton.text = "Continue"
            binding.otpLayout.visibility = View.GONE
            binding.etMobile.setText("")
            binding.registerDetailsLayout.visibility = View.GONE
        }

        binding.loginButton.setOnClickListener {
            if (binding.loginButton.text.toString() == "Login" || binding.loginButton.text.toString() == "Get Code") {
                if (login_with_otp) {
                    if (otp_layout_clicked) {
                        verifyOtp()
                    } else {
                        registerApiCall("BeforeOtp")
                    }
                } else {
                    loginApiCall()
                }
            } else if (binding.loginButton.text.toString() == "Continue") {
                if (otp_layout_clicked) {

                    verifyOtp()
                } else {
                    registerApiCall("BeforeOtp")
                }
            } else {
                registerApiCall("AfterOtp")
            }
        }

        binding.resend.setOnClickListener {
            sendOtp()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("710233511769-jdhjeivvjnhq3sbvkm2vb8osjojiuuv5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btGoogleAccount.setOnClickListener{
//            googleSignInClient.signOut()
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    if (account != null) {
                        g_name=account.displayName!!
                        g_email=account.email!!
                        g_id=account.id!!
                        googleLogin()

                        Toast.makeText(applicationContext,account.displayName,Toast.LENGTH_LONG).show()
                       // firebaseAuthWithGoogle(account.idToken!!)
                        Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("TAG", "Google sign in failed", e)
                    // ...

            }
        }
    }

    private fun loginUiUpdate() {
        binding.otpView.setText("")
        login_with_otp = false;
        binding.viewRegister.visibility = View.GONE
        binding.viewLogin.visibility = View.VISIBLE;
        binding.btGoogleAccount.visibility=View.VISIBLE
        binding.txtLogin.setTextColor(Color.parseColor("#005DA8"));
        binding.txtRegister.setTextColor(Color.parseColor("#ACACAC"));
        binding.loginEmailLayout.visibility = View.VISIBLE
        binding.loginOtpLayout.visibility = View.GONE
//        binding.loginOtp.visibility = View.VISIBLE
//        binding.loginOtp.text = "Click here to Login with &quot; One time password &quot; OTP";
        binding.txtTitle.text = "Welcome"
        binding.txtTitle.setTextColor(resources.getColor(R.color.blue_text))
        binding.txtSubTitle.text = "Login to your Account"
        binding.txtSubTitle.visibility = View.VISIBLE
        binding.loginButton.text = "Login";
        binding.loginLayout.visibility = View.VISIBLE
        binding.loginDetailsLayout.visibility = View.VISIBLE
        binding.otpLayout.visibility = View.GONE
        binding.registerDetailsLayout.visibility = View.GONE
        binding.etMobile.setText("")

        binding.etRegisterPassword.setText("")
        binding.etRegisterConfirmPassword.setText("")
        binding.etMobileRegister.setText("")

    }

    private fun verifyOtp() {
        if (registerOtpValidationCheck())
            return
        var request: HashMap<String, String> = HashMap()
        request["mobile_number"] = binding.etMobileRegister.text.toString()
        request["otpNumber"] = binding.otpView.text.toString()
        if(gmail_click){
            request["otpIdentifier"] = "verify_mobile"
            request["user_id"] = user_id
        }else{
            when {
                binding.loginButton.text.toString() == "Login" -> {
                    request["otpIdentifier"] = "site_login"
                }
                binding.loginButton.text.toString() == "Continue" -> {
                    if (forgot_password)
                        request["otpIdentifier"] = "forgot_password"
                    else
                        request["otpIdentifier"] = "site_register"
                }
                else -> {
                    request["otpIdentifier"] = "site_register"
                }
            }
        }

        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.verifyOtp(request)
            val response: LoginResponse = result.body()!!
            if (result.body() == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            if (result.body()?.status!!) {
                when {
                    binding.loginButton.text.toString() == "Login" -> {
                        loadingDialog.dismiss()
                        Preferences.getInstance(this@LoginActivity).isLogin = true
                        Preferences.getInstance(this@LoginActivity).userId = response.user_id
                        Preferences.getInstance(this@LoginActivity).userType = response.user_type
                        Preferences.getInstance(this@LoginActivity).userName = response.fullname
                        Preferences.getInstance(this@LoginActivity).userEmail = response.email
                        Preferences.getInstance(this@LoginActivity).userMobile = response.contact
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    binding.loginButton.text.toString() == "Continue" -> {
                        if (forgot_password){
                            binding.layoutRegistrationDetails.visibility = View.GONE
                            binding.loginButton.text = "Reset Password"
                            binding.otpLayout.visibility = View.GONE
                            binding.loginLayout.visibility = View.VISIBLE
                            binding.loginDetailsLayout.visibility = View.GONE
                            binding.registerDetailsLayout.visibility = View.VISIBLE
//                            binding.loginOtp.visibility = View.GONE
                            binding.loginOtpLayout.visibility = View.GONE
//                            binding.loginOtp.visibility = View.VISIBLE
                        }else{
                            binding.layoutRegistrationDetails.visibility = View.VISIBLE
                            binding.loginButton.text = "Create Account"
                            binding.otpLayout.visibility = View.GONE
                            binding.loginLayout.visibility = View.VISIBLE
                            binding.loginDetailsLayout.visibility = View.GONE
                            if(gmail_click) {
                                binding.etFullName.isEnabled=false;
                                binding.etMail.isEnabled=false;
                                binding.etFullName.setText(Preferences.getInstance(applicationContext).userName.toString())
                                binding.etMail.setText(Preferences.getInstance(applicationContext).userEmail.toString())
                                binding.etMobileRegister.setText(Preferences.getInstance(applicationContext).userMobile.toString())
                            }
                            binding.registerDetailsLayout.visibility = View.VISIBLE
//                            binding.loginOtp.visibility = View.GONE
                            binding.loginOtpLayout.visibility = View.GONE
                        }
                    }
                    else -> {
                        binding.layoutRegistrationDetails.visibility = View.VISIBLE
                        if(gmail_click) {
                            binding.etFullName.setText(Preferences.getInstance(applicationContext).userName.toString())
                            binding.etMail.setText(Preferences.getInstance(applicationContext).userEmail.toString())
                            binding.etMobileRegister.setText(Preferences.getInstance(applicationContext).userMobile.toString())
                        }
                        binding.loginButton.text = "Create Account"
                        binding.otpLayout.visibility = View.GONE
                        binding.loginLayout.visibility = View.VISIBLE
                        binding.loginDetailsLayout.visibility = View.GONE
                        binding.registerDetailsLayout.visibility = View.VISIBLE
//                        binding.loginOtp.visibility = View.GONE
                        binding.loginOtpLayout.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this@LoginActivity, response!!.message, Toast.LENGTH_SHORT)
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun registerApiCall(whereFrom: String) {
        if (whereFrom == "BeforeOtp") {
            if (registerMobileValidationCheck())
                return
            if(gmail_click){
                unregisterSendOtp()
            }else{
                sendOtp()
            }

        } else {
            if (registerFinalValidationCheck())
                return
            if (binding.loginButton.text.toString() == "Reset Password")
                updateNewPassword()
            else
                updateUserDetails()
        }
    }

    private fun updateNewPassword() {
        var request: HashMap<String, String> = HashMap()
        request["email"] = binding.etMobileRegister.text.toString()
        request["password"] = binding.etRegisterPassword.text.toString()
        request["confirm_password"] = binding.etRegisterConfirmPassword.text.toString()
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.updateNewPassword(request)
            val response: LoginResponse = result.body()!!
            if (response == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            loadingDialog.dismiss()
            Toast.makeText(this@LoginActivity, response!!.message, Toast.LENGTH_SHORT)
                .show()
            loginUiUpdate()
        }
    }

    private fun updateUserDetails() {
        var request: HashMap<String, String> = HashMap()
        request["reg_name"] = binding.etFullName.text.toString()
        request["reg_email"] = binding.etMail.text.toString()
        request["mobile_number"] = binding.etMobileRegister.text.toString()
        request["password"] = binding.etRegisterPassword.text.toString()
        request["confirm_password"] = binding.etRegisterConfirmPassword.text.toString()
        if(gmail_click){
            request["user_id"] = user_id
        }else{
            request["user_id"] = ""
        }
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.updateUserDetails(request)
            val response: LoginResponse = result.body()!!
            if (response == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            if (response.status!!) {

                Preferences.getInstance(this@LoginActivity).isLogin = true
                Preferences.getInstance(this@LoginActivity).userId = response.user_id
                Preferences.getInstance(this@LoginActivity).userType = response.user_type
                Preferences.getInstance(this@LoginActivity).userName = response.fullname
                Preferences.getInstance(this@LoginActivity).userEmail = response.email
                Preferences.getInstance(this@LoginActivity).userMobile = response.contact
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            loadingDialog.dismiss()
            Toast.makeText(this@LoginActivity, response!!.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun sendOtp() {
        var request: HashMap<String, String> = HashMap()
        request["mobile_number"] = binding.etMobileRegister.text.toString()
        when {
            binding.loginButton.text.toString() == "Login" -> {
                request["otpIdentifier"] = "site_login"
            }
            binding.loginButton.text.toString() == "Get Code" || forgot_password -> {
                request["otpIdentifier"] = "forgot_password"
            }
            else -> {
                request["otpIdentifier"] = "site_register"
            }
        }
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.sendOtp(request)
            if (result.body() == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: LoginResponse = result.body()!!
            if (response.status) {
                otp_layout_clicked = true
                binding.loginLayout.visibility = View.GONE
                binding.otpLayout.visibility = View.VISIBLE
        //        binding.loginOtp.visibility = View.GONE
                binding.otpMobile.text = "+91 " + binding.etMobileRegister.text
                if (binding.loginButton.text.toString() == "Get Code") {
                    binding.loginButton.text = "Continue"
                }
            }
            loadingDialog.dismiss()
            Toast.makeText(this@LoginActivity, response!!.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun unregisterSendOtp() {
        var request: HashMap<String, String> = HashMap()
        request["mobile_number"] = binding.etMobileRegister.text.toString()
        request["otpIdentifier"] = "verify_mobile"
        request["user_id"] = user_id

        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.sendOtp(request)
            if (result.body() == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: LoginResponse = result.body()!!
            if (response.status) {
                if(response.message.contains("Already Verified")){
                    forgot_password = false
                    gmail_click=false
                    loginUiUpdate()
                }else{
                    otp_layout_clicked = true
                    binding.loginLayout.visibility = View.GONE
                    binding.otpLayout.visibility = View.VISIBLE
                    binding.loginOtp.visibility = View.GONE
                    binding.otpMobile.text = "+91 " + binding.etMobileRegister.text
                    if (binding.loginButton.text.toString() == "Get Code") {
                        binding.loginButton.text = "Continue"
                    }
                }

            }
            loadingDialog.dismiss()
            Toast.makeText(this@LoginActivity, response!!.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun registerOtpValidationCheck(): Boolean {
        var bool: Boolean = false
        if (binding.otpView.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter otp", Toast.LENGTH_SHORT)
                .show()
            bool = true
            return bool
        }
        if (binding.otpView.text!!.length < 5) {
            Toast.makeText(this@LoginActivity, "Enter valid otp", Toast.LENGTH_SHORT)
                .show()
            bool = true
        }


        return bool;

    }

    private fun registerMobileValidationCheck(): Boolean {
        var bool: Boolean = false
        if (binding.etMobileRegister.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter email/mobile number", Toast.LENGTH_SHORT)
                .show()
            bool = true
            return bool
        }

        return bool;

    }

    private fun registerFinalValidationCheck(): Boolean {
        var bool: Boolean = false
        if (binding.loginButton.text.toString() != "Reset Password") {
            if (binding.etFullName.text.toString().isEmpty()) {
                Toast.makeText(this@LoginActivity, "Enter full name", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            if (binding.etMail.text.toString().isEmpty()) {
                Toast.makeText(this@LoginActivity, "Enter email address", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
        }
        if (binding.etRegisterPassword.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT)
                .show()
            return true
        }
        if (binding.etRegisterConfirmPassword.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter confirm password", Toast.LENGTH_SHORT)
                .show()
            return true
        }
        if (!binding.etRegisterPassword.text.toString()
                .contentEquals(binding.etRegisterConfirmPassword.text.toString())
        ) {
            Toast.makeText(
                this@LoginActivity,
                "Password and confirm password not match",
                Toast.LENGTH_SHORT
            )
                .show()
            return true
        }
        return bool;
    }


    private fun loginValidationCheck(): Boolean {
        var bool: Boolean = false
        if (binding.etMobile.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter email/mobile number", Toast.LENGTH_SHORT)
                .show()
            bool = true
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT)
                .show()
            bool = true;
        }

        return bool
    }

    private fun loginApiCall() {
        if (loginValidationCheck())
            return
        coroutineScope.launch {
            loadingDialog.show()
            val request = LoginRegisterRequest()
            request.email = binding.etMobile.text.toString()
            request.password = binding.etPassword.text.toString()
            val result = apiService.loginApi(request)
            if (result.body() == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            if (result.body()?.status!!) {
                val response: LoginResponse = result.body()!!
                loadingDialog.dismiss()
                Preferences.getInstance(this@LoginActivity).isLogin = true
                Preferences.getInstance(this@LoginActivity).userId = response.user_id
                Preferences.getInstance(this@LoginActivity).userType = response.user_type
                Preferences.getInstance(this@LoginActivity).userName = response.fullname
                Preferences.getInstance(this@LoginActivity).userEmail = response.email
                Preferences.getInstance(this@LoginActivity).userMobile = response.contact
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, result.body()!!.message, Toast.LENGTH_SHORT)
                    .show()
                loadingDialog.dismiss()
            }
        }
    }

    private fun googleLogin() {
        var request: HashMap<String, String> = HashMap()
        request["name"] = g_name
        request["email"] = g_email
        request["google_id"] = g_id

        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.googleLogin(request)
            val response: GoogleLoginResponse = result.body()!!
            if (response == null) {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            if (response.status!!) {

                gmail_click=false
                if(response.user_details.mobileVerified.equals("1")) {

                    Preferences.getInstance(this@LoginActivity).isLogin = true
                    Preferences.getInstance(this@LoginActivity).userId = response.user_details.userId
                    Preferences.getInstance(this@LoginActivity).userType = response.user_details.userType
                    Preferences.getInstance(this@LoginActivity).userName = response.user_details.userName
                    Preferences.getInstance(this@LoginActivity).userEmail = response.user_details.userEmail
                    Preferences.getInstance(this@LoginActivity).userMobile = response.user_details.userCon
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Please enter mobile number to verify",Toast.LENGTH_LONG).show()
                    user_id=response.user_details.userId
                    forgot_password = false
                    binding.otpView.setText("")
                    otp_layout_clicked = false
                    binding.orLoginWith.visibility=View.GONE
                    binding.btGoogleAccount.visibility=View.GONE
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.viewRegister.visibility = View.VISIBLE
                    binding.viewLogin.visibility = View.GONE
                    binding.loginEmailLayout.visibility = View.GONE
                    binding.loginOtpLayout.visibility = View.VISIBLE
                    binding.txtLogin.setTextColor(Color.parseColor("#ACACAC"))
                    binding.txtRegister.setTextColor(Color.parseColor("#005DA8"))
                    binding.loginOtp.visibility = View.GONE
                    binding.txtTitle.text = "Verify Mobile Number"
                    binding.txtSubTitle.visibility = View.GONE
                    binding.loginButton.text = "Continue"
                    binding.otpLayout.visibility = View.GONE
                    binding.etMobile.setText("")
                    Preferences.getInstance(this@LoginActivity).userId = response.user_details.userId
                    Preferences.getInstance(this@LoginActivity).userName = response.user_details.userName
                    Preferences.getInstance(this@LoginActivity).userEmail = response.user_details.userEmail
                    //Preferences.getInstance(this@LoginActivity).userMobile = response.user_details.userCon
                    binding.registerDetailsLayout.visibility = View.GONE


                }
            }
            loadingDialog.dismiss()

        }
    }

}