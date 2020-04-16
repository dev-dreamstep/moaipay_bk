package com.dreamstep.moaipay.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.Users
import com.dreamstep.moaipay.utils.BaseAdapter
import com.dreamstep.moaipay.utils.BaseViewHolder
import com.dreamstep.moaipay.utils.ViewUtils
import kotlinx.android.synthetic.main.item_member_icon.view.*

class MoaiAttendanceListAdapter: BaseAdapter<Users>() {

    lateinit var context: Context

    // FUNCTIONS
    // ====================================================
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_member_icon, parent, false)
        )
    }

    // VIEW HOLDERS
    // ====================================================
    inner class ViewHolder(itemView: View): BaseViewHolder(itemView) {

        // ====================================================
        override fun onBind(position: Int) {
            super.onBind(position)

            // Object ========> POST
            val users = mItemList[position]

            setView(users)

            // CLICKS LISTENERS
            // ====================================================
            itemView.setOnClickListener {
            }
        }

        private fun setView(users: Users){
            val name = users.name
            val avatar = users.avatar

            if (name.length ?: 0 > 8) {
                val text = name.substring(0, 8) + "..."
                ViewUtils.putText(itemView.lblUserName, text)
            } else {
                ViewUtils.putText(itemView.lblUserName, name)
            }
            if (!avatar.isNullOrEmpty()) {
                ViewUtils.renderImage(avatar, itemView.imgIcon, context)
            }
        }

    }
}

