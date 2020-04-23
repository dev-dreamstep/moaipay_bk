package com.dreamstep.moaipay.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.Members
import com.dreamstep.moaipay.utils.BaseAdapter
import com.dreamstep.moaipay.utils.BaseViewHolder
import com.dreamstep.moaipay.utils.ViewUtils
import kotlinx.android.synthetic.main.item_moai_detail_member.view.*

class MoaiDetailRowHeaderAdapter(
    private var recieved: ArrayList<String>
): BaseAdapter<Members>() {

    lateinit var context: Context

    // FUNCTIONS
    // ====================================================
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_moai_detail_member, parent, false)
        )
    }

    // VIEW HOLDERS
    // ====================================================
    inner class ViewHolder(itemView: View): BaseViewHolder(itemView) {

        // ====================================================
        override fun onBind(position: Int) {
            super.onBind(position)

            // Object ========> POST
            val member = mItemList[position]

            setView(member)

            // CLICKS LISTENERS
            // ====================================================
            itemView.setOnClickListener {
            }
        }

        private fun setView(member: Members){
            val id = member.userId
            val name = member.name
            val avatar = member.avatar

            ViewUtils.putText(itemView.lblName, name)
            ViewUtils.renderImage(avatar, itemView.imgIcon, context)
            itemView.iconInfo.visibility = View.INVISIBLE
        }

    }
}

