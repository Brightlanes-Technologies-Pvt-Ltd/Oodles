package com.ias.gsscore.network.notification

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class NotificationResponse (
  @SerializedName("notification_list" ) var list : ArrayList<NotificationList> = arrayListOf()
): OodlesApiResponse()