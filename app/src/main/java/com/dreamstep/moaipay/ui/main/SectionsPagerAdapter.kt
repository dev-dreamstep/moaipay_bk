package com.dreamstep.moaipay.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.fragment.main.MoaiFragment
import com.dreamstep.moaipay.fragment.moaiList.MoaiListFragment
import com.dreamstep.moaipay.fragment.moaiList.PlaceholderFragment
import com.dreamstep.moaipay.fragment.mypage.SettingsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4,
    R.string.tab_text_5
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment

        when(position) {
            0 -> fragment = MoaiFragment.newInstance(1)
            1 -> fragment = MoaiListFragment.newInstance()
            2 -> fragment = SettingsFragment.newInstance()
            else -> fragment = PlaceholderFragment.newInstance(position + 1)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}