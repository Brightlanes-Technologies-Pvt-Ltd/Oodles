package com.ias.gsscore.network.response.google

data class UserDetails(
    var userId:String,
    var userName:String,
    var userEmail:String,
    var userType:String,
    var mobileVerified:String,
    var emailVerified:String,
    var userCon:String
    )