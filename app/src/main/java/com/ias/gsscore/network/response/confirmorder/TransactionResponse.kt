package com.ias.gsscore.network.response.confirmorder


data class TransactionResponse (
  var status: Boolean = false,
  var error: String = "Something went wrong",

 )