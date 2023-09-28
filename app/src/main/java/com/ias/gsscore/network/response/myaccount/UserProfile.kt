package com.ias.gsscore.network.response.myaccount


data class UserProfile (
    var id: String,
    var user_id: String,
    var mobile:String,
    var email: String,
    var fullname:String,
    var contact:String,
    var user_type:String
)