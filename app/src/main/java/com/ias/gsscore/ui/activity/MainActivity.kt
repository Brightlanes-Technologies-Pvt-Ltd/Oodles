package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityMainBinding
import com.ias.gsscore.network.response.myaccount.MainsTest
import com.ias.gsscore.ui.fragment.CourseTestFragment
import com.ias.gsscore.ui.viewmodel.MainViewModel
import com.ias.gsscore.uploadFiles.FILE_TYPE
import com.ias.gsscore.uploadFiles.FILE_URL
import com.ias.gsscore.uploadFiles.FileUploadWorker
import com.ias.gsscore.uploadFiles.PDFUploadListener
import com.ias.gsscore.uploadFiles.PROGRAM_ID
import com.ias.gsscore.uploadFiles.ROLL_NO
import com.ias.gsscore.uploadFiles.TEST_ID
import com.ias.gsscore.uploadFiles.UPLOAD_CHANNEL_DESC
import com.ias.gsscore.uploadFiles.USER_ID
import com.ias.gsscore.uploadFiles.USER_NAME
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity(), PDFUploadListener {
    lateinit var binding: ActivityMainBinding
    var fromEdit=false
    var pos=""
    var mainsTest : MainsTest? = null
    private val BUFFER_SIZE = 1024 * 2
    private val PDF_DIRECTORY = "/GSscore"
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[MainViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = this
        fromEdit=intent.getBooleanExtra("fromEdit",false)
        pos= intent.getStringExtra("pos").toString()
        SingletonClass.instance.setCart(binding.cartCount,binding.countLayout)
        val navController = findNavController(R.id.myNavHostFragment)
        SingletonClass.instance.setCustomNavController(navController)
        SingletonClass.instance.setSupportManager(supportFragmentManager)

        if(fromEdit){
            when (pos) {
                "1" -> {
                    Preferences.getInstance(applicationContext).frTitle=""
                    MainViewModel.setHeaderTitle(0, "")
                    viewModel.isSelected.set(1)
                    val bundle = bundleOf("id" to "")
                    navController.navigate(R.id.courseFragment,bundle)
                }
                "2" -> {
                    Preferences.getInstance(applicationContext).frTitle=""
                    MainViewModel.setHeaderTitle(0, "")
                    viewModel.isSelected.set(2)
                    navController.navigate(R.id.studyNotesFragment)
                }
                "3" -> {
                    Preferences.getInstance(applicationContext).frTitle=""
                    MainViewModel.setHeaderTitle(0, "")
                    viewModel.isSelected.set(0)
                    navController.navigate(R.id.homeFragment)
                }
                "4" -> {
                    Preferences.getInstance(applicationContext).frTitle=""
                    MainViewModel.setHeaderTitle(0, "")
                    viewModel.isSelected.set(3)
                    navController.navigate(R.id.materialFragment)
                }
                "5" -> {
                    Preferences.getInstance(applicationContext).frTitle=""
                    MainViewModel.setHeaderTitle(0, "")
                    viewModel.isSelected.set(4)
                    navController.navigate(R.id.myAccountFragment)
                }
            }
        }

        binding.homeFragment.setOnClickListener {
            binding.btCart.visibility = View.GONE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(0)
            navController.navigate(R.id.homeFragment)
        }
        binding.courseFragment.setOnClickListener {
            binding.btCart.visibility = View.GONE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(1)
            val bundle = bundleOf("id" to "")
            navController.navigate(R.id.courseFragment,bundle)
        }

        binding.studyNotesFragment.setOnClickListener {
            binding.btCart.visibility = View.VISIBLE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(2)
            navController.navigate(R.id.studyNotesFragment)
        }
        binding.materialFragment.setOnClickListener {
            binding.btCart.visibility = View.GONE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(3)
            navController.navigate(R.id.materialFragment)
        }

        binding.myAccountFragment.setOnClickListener {
            binding.btCart.visibility = View.GONE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(4)
            navController.navigate(R.id.myAccountFragment)
        }

        binding.myCourse.setOnClickListener{
            binding.btCart.visibility = View.GONE
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(4)
            navController.navigate(R.id.myAccountFragment)
            binding.studyNotes.setBackgroundResource(0)
            binding.myCourse.setBackgroundResource(R.drawable.nav_selec_bg)
            binding.resourse.setBackgroundResource(0)
            binding.currentAffair.setBackgroundResource(0)
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.tvPlans.setOnClickListener{
            binding.studyNotes.setBackgroundResource(R.drawable.nav_selec_bg)
            binding.myCourse.setBackgroundResource(0)
            binding.resourse.setBackgroundResource(0)
            binding.currentAffair.setBackgroundResource(0)
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(2)
            navController.navigate(R.id.studyNotesFragment)
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.tvHistory.setOnClickListener{
            binding.studyNotes.setBackgroundResource(0)
            binding.myCourse.setBackgroundResource(0)
            binding.resourse.setBackgroundResource(R.drawable.nav_selec_bg)
            binding.currentAffair.setBackgroundResource(0)
            Preferences.getInstance(applicationContext).frTitle=""
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(3)
            navController.navigate(R.id.materialFragment)
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.tvSupport.setOnClickListener{
            binding.studyNotes.setBackgroundResource(0)
            binding.myCourse.setBackgroundResource(0)
            binding.resourse.setBackgroundResource(0)
            binding.currentAffair.setBackgroundResource(R.drawable.nav_selec_bg)
            Preferences.getInstance(this).frTitle="PIB Compilation"+";"+"true"+";"+"22"
            MainViewModel.setHeaderTitle(0, "")
            viewModel.isSelected.set(3)
            navController.navigate(R.id.materialFragment)
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.tvNotification.setOnClickListener{
            startActivity(Intent(this,NotificationActivity::class.java))
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        registerResultLauncher()
    }

    private fun registerResultLauncher() {
        // Initialize result launcher
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                // Initialize result data
                val data: Intent? = result.data
                // check condition
                if (data != null) {
                    val uri: Uri = data?.data!!
                    val uriString: String = uri.toString()
                    if (uriString.startsWith("content://")) {
                        val pdfUrl = getFilePathFromURI(this, uri)
                        var myCursor: Cursor? = null
                        try {
                            myCursor =
                                applicationContext!!.contentResolver.query(
                                    uri,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            if (myCursor != null && myCursor.moveToFirst()) {
                                @SuppressLint("Range")
                                val data: Data = Data.Builder()
                                    .putString(
                                        UPLOAD_CHANNEL_DESC,
                                        "The task data passed from MainActivity"
                                    )
                                    .putString(FILE_TYPE, "PDF")
                                    .putString(FILE_URL, pdfUrl)
                                    .putString(USER_ID, Preferences.getInstance(this).userId)
                                    .putString(PROGRAM_ID, mainsTest?.programId)
                                    .putString(TEST_ID, mainsTest?.testId)
                                    .putString(ROLL_NO, Preferences.getInstance(this).userId)
                                    .putString(USER_NAME, Preferences.getInstance(this).userName)
                                    .build()
                                val workRequest = OneTimeWorkRequest.Builder(FileUploadWorker::class.java).setInputData(data).build()
                                val workManager = WorkManager.getInstance(SingletonClass.instance)
                                workManager.enqueueUniqueWork("pdfUpload",
                                    ExistingWorkPolicy.REPLACE ,workRequest)
                                val workInfoLiveData = workManager.getWorkInfoByIdLiveData(workRequest.id)
                                workInfoLiveData.observe(this) { workInfo ->
                                    if (workInfo != null && workInfo.state === WorkInfo.State.SUCCEEDED) {
                                        val outputData = workInfo.outputData
                                        mainsTest?.copyType = "submited"
                                        onPdfUploaded()
                                        // Use the result in your Activity
                                    }
                                }
                            }
                        } finally {
                            myCursor?.close()
                        }
                    }
                }
            })
    }


    override fun selectPDF(mainsTest: MainsTest) {
        this.mainsTest = mainsTest
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                  arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE ), 1)
        }else {
            selectPdf()
        }
    }

    override fun viewPdf(pdfUrl: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        val chooser = Intent.createChooser(browserIntent, getString(R.string.pdfViewer))
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK // optional
        startActivity(chooser)
    }

    private fun selectPdf() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        resultLauncher?.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getFilePathFromURI(context: Context, contentUri: Uri?): String? {
        //copy file and send new file path
        val fileName = getFileName(contentUri)
        val downloadDirectory = File(
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + PDF_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(downloadDirectory.toString() + File.separator + fileName)
            // create folder if not exists
            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    private fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    private fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val inStream = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (inStream.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
            try {
                inStream.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
        }
        return count
    }


    override fun onBackPressed() {
        MainViewModel.setHeaderTitle(0, "")
        super.onBackPressed()
    }

    private fun onPdfUploaded() {
        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = this.supportFragmentManager.findFragmentByTag(CourseTestFragment::javaClass.name)
            if (fragment is CourseTestFragment) {
                fragment.type = "mains-test"
                fragment.onResume()
            }
        }, 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf()
        }
        else {
            Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}