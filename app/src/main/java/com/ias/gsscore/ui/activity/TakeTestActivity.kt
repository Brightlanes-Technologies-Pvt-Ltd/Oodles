package com.ias.gsscore.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.databinding.ActivityTakeTestBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.viewmodel.TakeTestViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi

class TakeTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTakeTestBinding
    lateinit var viewModel: TakeTestViewModel
    private var testId = ""
    private var programId = ""
    lateinit var loadingDialog: AlertDialog

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakeTestBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[TakeTestViewModel::class.java]

        setContentView(binding.root)
        binding.viewmodel = viewModel
        activity = this
        viewModel.context = this
        viewModel.contextActivity = this
        viewModel.db = SingletonClass.instance.dbInstance(this)
        loadingDialog = RetrofitHelper.loadingDialog(this)
        viewModel.loadingDialog = loadingDialog
        viewModel.supportFragmentManager = supportFragmentManager
        val title = intent.getStringExtra("title").toString()
        testId = intent.getStringExtra("testId").toString()
        programId = intent.getStringExtra("programId").toString()
        binding.tvTitle.text = title
        viewModel.startTest(testId,programId)

      /*  //Room DB
        val db = SingletonClass.instance.dbInstance(this)
        // insert data
        val data = TestModel(0,programId,testId)
        GlobalScope.launch(Dispatchers.Main) {
            db.oodlesDao().insert(data)
        }*/
       /* //Room DB get all data
        GlobalScope.launch(Dispatchers.Main) {
            val dataList = SingletonClass.instance.dbInstance(this@TakeTestActivity).locationDao().getAll()
        }
        //Room DB update data
        GlobalScope.launch(Dispatchers.IO) {
           // db.locationDao().update(stateData)
        }*/

    }

    companion object {
        var activity: TakeTestActivity? = null
        fun getInstance(): TakeTestActivity {
            return activity!!
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()
    }


}