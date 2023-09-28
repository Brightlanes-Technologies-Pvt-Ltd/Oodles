package com.ias.gsscore.network.response.myaccount

import com.ias.gsscore.network.response.OodlesApiResponse


data class ProfileResponse (
    var user_profile: UserProfile
): OodlesApiResponse()