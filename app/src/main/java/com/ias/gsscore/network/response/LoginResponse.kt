package com.ias.gsscore.network.response

data class LoginResponse(
    var status:Boolean,
    var message:String,
    var user_id:String,
    var username:String,
    var fullname:String,
    var email:String,
    var contact:String,
    var display_name:String,
    var user_type:String,
    var mobileVerified:String,
    var emailVerified:String
    )