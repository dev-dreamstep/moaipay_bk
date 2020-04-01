package com.dreamstep.moaipay.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var currentPosition: Int = 0

    /**
     * Override for default values or clearing itemView
     */
    open fun clear(){}
    open fun onBind(position: Int) {
        currentPosition = position
        clear()
    }

}