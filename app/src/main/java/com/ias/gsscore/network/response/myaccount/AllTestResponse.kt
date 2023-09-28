package com.ias.gsscore.network.response.myaccount

import com.example.example.VideoList
import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class AllTestResponse (
    @SerializedName("package_detail") var packageDetail : PackageDetail?    = PackageDetail(),
    @SerializedName("program_list") var programList: ArrayList<ProgramList> = arrayListOf(),
    @SerializedName("pt_test_list") var ptTest: ArrayList<PtTest> = arrayListOf(),
  //@SerializedName("package_program_details" ) var packageProgramDetails : ArrayList<PackageProgramDetails> = arrayListOf(),
    @SerializedName("mains_test_list") var mainsTest: ArrayList<MainsTest> = arrayListOf(),
    @SerializedName("ebook_lists") var ebookList: ArrayList<EbookList> = arrayListOf(),
    @SerializedName("video_lists") var videoList: ArrayList<VideoList> = arrayListOf(),



    ):OodlesApiResponse()