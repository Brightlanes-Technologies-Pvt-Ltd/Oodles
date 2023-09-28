package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class EbookList (

  @SerializedName("program_id"    ) var programId    : String? = null,
  @SerializedName("program_title" ) var programTitle : String? = null,
  @SerializedName("has_child"     ) var hasChild     : String? = null,
  @SerializedName("ebook_title"   ) var ebookTitle   : String? = null,
  @SerializedName("file_url"      ) var fileUrl      : String? = null,
  @SerializedName("ebook_id"      ) var ebookId      : String? = null

)