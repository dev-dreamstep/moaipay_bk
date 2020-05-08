package com.dreamstep.moaipay.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dreamstep.moaipay.fragment.main.MoaiDetailFragment
import com.dreamstep.moaipay.fragment.main.MoaiInfoFragment

const val PAGE_INDEX_MOAI_DETAIL = 0
const val PAGE_INDEX_MOAI_INFOMATION = 1

class MoaiDetailPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

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
