package com.ias.gsscore.network.response.address

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class AddressResponse (
  @SerializedName("address_list" ) var addressList : ArrayList<AddressList> = arrayListOf(),
  @SerializedName("adress_details" ) var addressDetails : AddressList
): OodlesApiResponse()