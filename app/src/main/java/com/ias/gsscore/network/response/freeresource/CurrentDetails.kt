package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class CurrentDetails (

  @SerializedName("id" ) var id      : String? = null,
  @SerializedName("title" ) var title      : String? = null,
  @SerializedName("description" ) var description      : String? = null,
  @SerializedName("file_url" ) var file_url      : String? = null,
  @SerializedName("meta_image" ) var meta_image      : String? = null,
  @SerializedName("type_name" ) var type_name      : String? = null,
  @SerializedName("cat_title" ) var category      : String? = null,
  @SerializedName("image_url" ) var image_url      : String? = null,
  @SerializedName("postedOn" ) var postedOn      : String? = null,
  @SerializedName("sub_article_list" ) var sub_article_list  : ArrayList<FreeResourceList> = arrayListOf(),




)