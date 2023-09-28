package com.ias.gsscore.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ias.gsscore.ui.fragment.*

private const val NUM_TABS = 4

class MentorBoxViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle?, context1: Context) :
    FragmentStateAdapter(fm, lifecycle!!) {
    val context = context1
    override fun createFragment(i: Int): Fragment {
        return when (i) {
            0 -> {
                MentorChatFragment()
            }
            1 -> {
                MentorPostFragment()
            }
            2 -> {
                MentorsFragment()
            }
            else -> {
                MentorsEventsFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }
}