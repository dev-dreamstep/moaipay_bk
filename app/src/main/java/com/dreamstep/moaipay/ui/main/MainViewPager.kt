package com.dreamstep.moaipay.ui.main

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class MainViewPager(context: Context, attributeSet: AttributeSet?) : ViewPager(context, attributeSet) {

    override fun arrowScroll(direction: Int): Boolean {
        return false
    }
}