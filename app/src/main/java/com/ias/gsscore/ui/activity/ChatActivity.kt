package com.ias.gsscore.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityChatBinding
import com.ias.gsscore.ui.adapter.ChatViewPagerAdapter
import com.ias.gsscore.ui.viewmodel.ChatViewModel
import com.ias.gsscore.ui.viewmodel.HeaderBackViewModel
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory


@Suppress("DEPRECATION")
class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding;
    lateinit var viewModel: ChatViewModel
    private lateinit var headerBackViewModel: HeaderBackViewModel
    var tabPosition = 0;
    private var tabName = arrayOf(
        "Chat",
        "Tag Chat",
        "Assigned",
        "Events"
    )
    private var tabIcon = arrayOf(
        R.drawable.ic_chat_unselected,
        R.drawable.ic_tag_chat_unselected,
        R.drawable.ic_assigned_unselected,
        R.drawable.ic_events_unselected
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabPosition = intent.getIntExtra("position",0);
        binding = ActivityChatBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[ChatViewModel::class.java]
        headerBackViewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[HeaderBackViewModel::class.java]
        setContentView(binding.root)
        binding.viewmodel = viewModel
        binding.headerBack.backHeaderViewModel = headerBackViewModel
        viewModel.context = this
        headerBackViewModel.context = this
        setTabLayout()
    }

    private fun setTabLayout() {
        var viewPagerAdapter = ChatViewPagerAdapter(supportFragmentManager, lifecycle,this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.tabRippleColor = null
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            Log.e("TAG", "setTabLayout: $position")
            tab.text = tabName[position]
            tab.setIcon(tabIcon[position])
        }.attach()
        binding.headerBack.tvTitle.text = tabName[tabPosition]
        binding.tabLayout.selectTab( binding.tabLayout.getTabAt(tabPosition))
        val tabIconColor = ContextCompat.getColor(this@ChatActivity, R.color.blue_text)
        binding.tabLayout.getTabAt(tabPosition)!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        })



        binding.tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(this@ChatActivity, R.color.blue_text)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                binding.headerBack.tvTitle.text = tabName[tab.position]
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(this@ChatActivity, R.color.tab_unselected)
                tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    fun toast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}