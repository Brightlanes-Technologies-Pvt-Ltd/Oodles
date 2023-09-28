package com.ias.gsscore.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ias.gsscore.ui.fragment.AssignedFragment
import com.ias.gsscore.ui.fragment.ChatFragment
import com.ias.gsscore.ui.fragment.EventsFragment

private const val NUM_TABS = 4

class ChatViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle?, context1: Context) :
    FragmentStateAdapter(fm, lifecycle!!) {
    val context = context1
    override fun createFragment(i: Int): Fragment {
        return when (i) {
            0 -> {
                ChatFragment()
            }
            1 -> {
                ChatFragment()
            }
            2 -> {
                AssignedFragment()
            }
            else -> {
                 EventsFragment()
            }
        }
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }
}