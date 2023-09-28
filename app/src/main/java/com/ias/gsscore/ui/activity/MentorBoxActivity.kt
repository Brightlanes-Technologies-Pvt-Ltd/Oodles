package com.ias.gsscore.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.R
import com.ias.gsscore.ui.adapter.MentorBoxViewPagerAdapter

class MentorBoxActivity : BaseActivity() {
    @BindView(R.id.viewPager)
    lateinit var viewPager: ViewPager2 
    @BindView(R.id.tabLayout)
    lateinit var tabLayout: TabLayout
    private var tabName = arrayOf(
        "Chat",
        "Post",
        "Mentors",
        "Events"
    )
    private var tabIcon = arrayOf(
        R.drawable.ic_chat_unselected,
        R.drawable.ic_post_unselected,
        R.drawable.ic_user_grey,
        R.drawable.ic_events_unselected
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_box)
        ButterKnife.bind(this)
        setToolBar("IAS Foundation 2020")
        hideFooter(true)
        setTabLayout()
    }

    private fun setTabLayout() {
        var viewPagerAdapter = MentorBoxViewPagerAdapter(supportFragmentManager, lifecycle,this)
        viewPager.adapter = viewPagerAdapter
        tabLayout.tabRippleColor = null
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            Log.e("TAG", "setTabLayout: $position")
            tab.text = tabName[position]
            tab.setIcon(tabIcon[position])
        }.attach()
        val tabPosition = 0
        tabLayout.selectTab( tabLayout.getTabAt(tabPosition))
        val tabIconColor = ContextCompat.getColor(this, R.color.blue_text)
        tabLayout.getTabAt(tabPosition)!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        })



        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(this@MentorBoxActivity, R.color.blue_text)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                if(tab.position==0)
                    hideFooter(true)
                else
                    hideFooter(false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(this@MentorBoxActivity, R.color.tab_unselected)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}