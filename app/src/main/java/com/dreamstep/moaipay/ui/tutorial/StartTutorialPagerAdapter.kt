package com.dreamstep.moaipay.ui.tutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dreamstep.moaipay.fragment.tutorial.StartTutorialFragment

const val PAGE_INDEX_FIRST = 0
const val PAGE_INDEX_SECOND = 1
const val PAGE_INDEX_LAST = 2

class StartTutorialPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return PAGE_INDEX_LAST + 1
    }

    override fun getItem(position: Int): Fragment {
        return StartTutorialFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}