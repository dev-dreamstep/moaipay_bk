package com.dreamstep.moaipay.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.activity.MainActivity
import com.dreamstep.moaipay.fragment.chat.ChatFragment
import com.dreamstep.moaipay.fragment.dummy.DummyFragment
import com.dreamstep.moaipay.fragment.main.MoaiFragment
import com.dreamstep.moaipay.fragment.main.MoaiStartFragment
import com.dreamstep.moaipay.fragment.profile.ProfileSettingsFragment
import com.dreamstep.moaipay.interfaces.callback.MainTabCallback

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

    var startFragment = MainActivity.StartFragment.MOAI_MAIN

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
//        val mainTab = context as MainTabCallback

        when(position) {
            0 -> {
                fragment = ChatFragment.newInstance("", "")
            }
            1 -> {
                when (startFragment) {
                    MainActivity.StartFragment.MOAI_START -> {
                        fragment = MoaiStartFragment.newInstance()
                    }
                    else -> fragment = MoaiFragment.newInstance()
                }
            }
            else -> {
                fragment = ProfileSettingsFragment.newInstance()
            }
        }

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}