package com.ias.gsscore.network.response.google

data class GoogleLoginResponse(
    var status:Boolean,
    var user_details:UserDetails,
    )