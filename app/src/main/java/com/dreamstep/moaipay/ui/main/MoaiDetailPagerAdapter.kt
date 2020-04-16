package com.dreamstep.moaipay.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.activity.MainActivity
import com.dreamstep.moaipay.fragment.dummy.DummyFragment
import com.dreamstep.moaipay.fragment.main.MoaiDetailFragment
import com.dreamstep.moaipay.fragment.main.MoaiFragment
import com.dreamstep.moaipay.fragment.main.MoaiInfoFragment
import com.dreamstep.moaipay.fragment.main.MoaiStartFragment
import com.dreamstep.moaipay.interfaces.callback.MainTabCallback

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class MoaiDetailPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment

        when(position) {
            0 -> fragment = MoaiDetailFragment.newInstance()
            else -> fragment = MoaiInfoFragment.newInstance()
        }

        return fragment
    }

    override fun getCount(): Int {
        return 2
    }
}
