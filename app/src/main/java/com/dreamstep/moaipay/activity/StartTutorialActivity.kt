package com.dreamstep.moaipay.activity

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.ui.tutorial.*
import kotlinx.android.synthetic.main.activity_start_tutorial.*

class StartTutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_tutorial)

        setupViewPager()
        setOnClickListeners()
    }

    private fun setupViewPager() {

        view_pager.setAdapter(StartTutorialPagerAdapter(supportFragmentManager))

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                changeCurrentPage(position, false)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setOnClickListeners() {

        next_tv.setOnClickListener {
            view_pager.run {
                when (currentItem) {
                    PAGE_INDEX_LAST -> {
                        val intent = Intent(this@StartTutorialActivity, ProfileSettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> changeCurrentPage(currentItem + 1, true)
                }
            }
        }

        skip_tv.setOnClickListener {
            val intent = Intent(this@StartTutorialActivity, ProfileSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changeCurrentPage(index: Int, changePage: Boolean = true) {

        var indicatorImgRes = R.mipmap.tutorial_navi1
        var buttonTextRes = R.string.next_tutorial

        when (index) {
            PAGE_INDEX_FIRST -> {
            }
            PAGE_INDEX_SECOND -> {
                indicatorImgRes = R.mipmap.tutorial_navi2
            }
            PAGE_INDEX_LAST -> {
                indicatorImgRes = R.mipmap.tutorial_navi3
                buttonTextRes = R.string.regist_profile
            }
        }

        indicator_iv.setImageResource(indicatorImgRes)
        next_tv.setText(buttonTextRes)

        if (changePage) {
            view_pager.currentItem = index
        }
    }

}
