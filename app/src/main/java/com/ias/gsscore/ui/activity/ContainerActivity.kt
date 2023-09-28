package com.ias.gsscore.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.ui.fragment.CourseDetailsFragment
import com.ias.gsscore.ui.fragment.CourseTestFragment
import com.ias.gsscore.utils.SingletonClass

class ContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        ButterKnife.bind(this)
        hideFooter(true)
        if (intent.getStringExtra("whereFrom") == "viewAllBatches") {
            val courseTestFragment =  CourseTestFragment(this, "Upcoming Batches", false, "")
            addFragment(courseTestFragment, this, CourseTestFragment::javaClass.name)
            setToolBar("All Batches")
            SingletonClass.instance.setContainerManager(supportFragmentManager)
        } else {
            setToolBar("IAS Foundation 2023")
            addFragment(CourseDetailsFragment(), this, CourseDetailsFragment::javaClass.name)
        }

    }

    companion object {
        fun addFragment(fragment: Fragment?, requireActivity: FragmentActivity, fragmentTag : String) {
            if (fragment == null) return
            requireActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.rootContainer, fragment, fragmentTag)
                .commitAllowingStateLoss()
        }
    }
}