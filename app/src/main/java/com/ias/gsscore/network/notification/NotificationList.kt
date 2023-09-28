package com.ias.gsscore.network.notification

import com.google.gson.annotations.SerializedName


data class NotificationList (
  @SerializedName("id"              ) var id             : String? = null,
  @SerializedName("title"        ) var title       : String? = null,
  @SerializedName("message"          ) var message         : String? = null,
  @SerializedName("date"          ) var date         : String? = null

)