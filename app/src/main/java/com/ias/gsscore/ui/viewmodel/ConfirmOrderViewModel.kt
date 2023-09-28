package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityConfirmOrderBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.address.AddressList
import com.ias.gsscore.network.response.confirmorder.*
import com.ias.gsscore.ui.activity.HashGeneration
import com.ias.gsscore.ui.activity.OrderPlacedActivity
import com.ias.gsscore.utils.Preferences
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF1
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_UDF2
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ConfirmOrderViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as ActivityConfirmOrderBinding
    lateinit var productCheckout: ProductCheckoutResponse
    lateinit var courseCheckout: CourseCheckoutResponse
    lateinit var payuResponse: PayUResponse
    lateinit var tranResponse: TransactionResponse
     var selectedAddress: AddressList = AddressList()
   lateinit var type:String
   var amount=""
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    var merchantId = "5492541"
    private var merchantkey = "kTJpmHeo"
    var merchantSalt = "W4fWQdToSa"
    var fromCart: Boolean = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)

    fun backPress() {
        (context as Activity).onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    fun productCheckout() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["method"] = "2"
        request["coupon_code"] = ""
        coroutineScope.launch {
            loadingDialog.show()

            if (fromCart) {
                var result = apiService.produtCheckout(request)
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
                productCheckout = result.body()!!
                if (productCheckout.status) {
                    startpay(productCheckout.message)


                } else {
                    Toast.makeText(
                        context,
                        productCheckout.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                var result1 = apiService.courseCheckout(request)
                if (result1.body() == null) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_msg),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    loadingDialog.dismiss()
                    return@launch
                }
                courseCheckout = result1.body()!!
                if (courseCheckout.status) {
                    startpay(courseCheckout.message)

                } else {
                    Toast.makeText(
                        context,
                        courseCheckout.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            loadingDialog.dismiss()


        }


    }

    @SuppressLint("SetTextI18n")
    fun payumonetResponse(payUResponse: Any?, merchantResponse: Any?) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["transaction_response"] = payUResponse.toString()
        if (fromCart) {
            request["type"] = "product"
        } else {
            request["type"] = "course"
        }
        coroutineScope.launch {
            loadingDialog.show()


            var result = apiService.payuresponse(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }else {
                payuResponse = result.body()!!
                if (payuResponse.status) {
                    var intent = Intent(context, OrderPlacedActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context,
                        payuResponse.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }


            loadingDialog.dismiss()
        }


    }

    @SuppressLint("SetTextI18n")
    fun transactionResponse(tr_id:String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["transaction_response"] =tr_id

        coroutineScope.launch {
            loadingDialog.show()


            var result = apiService.transresponse(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }else {
                tranResponse = result.body()!!
                if (payuResponse.status) {
                    var intent = Intent(context, OrderPlacedActivity::class.java)
                    context.startActivity(intent)


                } else {
                    Toast.makeText(
                        context,
                        payuResponse.error,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }


            loadingDialog.dismiss()
        }


    }


    fun startpay(message: Message?) {
        val additionalParams: HashMap<String, Any?> = HashMap()
        additionalParams[CP_UDF1] = selectedAddress.id!!
        if (fromCart) {
            additionalParams[CP_UDF2] = "product"
        } else {
            additionalParams[CP_UDF2] = "course"
        }

        val payUPaymentParams = PayUPaymentParams.Builder()
            .setAmount(amount)
            .setIsProduction(true)
            .setKey(merchantkey)
            .setProductInfo("address_id:"+selectedAddress.id)
            .setPhone(Preferences.getInstance(context).userMobile)
            .setTransactionId(message!!.txnid)
            .setFirstName(Preferences.getInstance(context).userName)
            .setEmail(Preferences.getInstance(context).userEmail)
            .setSurl("https://iasscore.in/api/payment_response/payumoney")
            .setFurl("https://iasscore.in/api/payment_response/payumoney")
            .setUserCredential(Preferences.getInstance(context).userName)
            .setAdditionalParams(additionalParams)
        .build()



        PayUCheckoutPro.open(
            (context as Activity), payUPaymentParams,
            object : PayUCheckoutProListener {


                    override fun onPaymentSuccess(response: Any) {
                    response as HashMap<*, *>
                    var intent = Intent(context, OrderPlacedActivity::class.java)
                    context.startActivity(intent)
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    //transactionResponse(response)
                    //transactionResponse(message.txnid!!)
                    //payumonetResponse(payUResponse, merchantResponse )

                }


                override fun onPaymentFailure(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    Toast.makeText(context,"Payment Failed",Toast.LENGTH_LONG).show()
                    (context as Activity).finish()
                }


                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    Toast.makeText(context,"Payment Cancelled",Toast.LENGTH_LONG).show()
                    (context as Activity).finish()
                }


                override fun onError(errorResponse: ErrorResponse) {
                    val errorMessage: String
                    if (errorResponse != null && errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                        errorMessage = errorResponse.errorMessage!!
                    else
                        errorMessage = "Error in payment"
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                }

                override fun generateHash(
                    valueMap: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    if (valueMap.containsKey(PayUCheckoutProConstants.CP_HASH_STRING)
                        && valueMap.containsKey(PayUCheckoutProConstants.CP_HASH_STRING) != null
                        && valueMap.containsKey(PayUCheckoutProConstants.CP_HASH_NAME)
                        && valueMap.containsKey(PayUCheckoutProConstants.CP_HASH_NAME) != null
                    ) {

                        val hashData = valueMap[PayUCheckoutProConstants.CP_HASH_STRING]
                        val hashName = valueMap[PayUCheckoutProConstants.CP_HASH_NAME]
                        var hash: String? = null


                        //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                        if (hashName.equals(
                                PayUCheckoutProConstants.CP_LOOKUP_API_HASH,
                                ignoreCase = true
                            )
                        ) {

                            //Calculate HmacSHA1 hash using the hashData and merchant secret key
                            hash = HashGeneration.generateHashFromSDK(
                                hashData!!,
                                merchantSalt,
                                merchantkey
                            )
                        } else {
                            //calculate SDH-512 hash using hashData and salt
                            hash = HashGeneration.generateHashFromSDK(
                                hashData!!,
                                merchantSalt
                            )
                        }
                        if (!TextUtils.isEmpty(hash)) {
                            val dataMap: HashMap<String, String?> = HashMap()
                            dataMap[hashName!!] = hash!!
                            hashGenerationListener.onHashGenerated(dataMap)
                        }
                    }
                }
            })
    }


}

