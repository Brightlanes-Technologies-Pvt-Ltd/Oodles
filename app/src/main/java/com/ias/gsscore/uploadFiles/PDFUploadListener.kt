package com.ias.gsscore.uploadFiles

import com.ias.gsscore.network.response.myaccount.MainsTest

interface PDFUploadListener {
    fun selectPDF(mainsTest: MainsTest)
    fun viewPdf(pdfUrl: String?)
}