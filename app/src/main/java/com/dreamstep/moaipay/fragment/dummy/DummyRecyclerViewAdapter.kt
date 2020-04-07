package com.dreamstep.moaipay.fragment.dummy

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dreamstep.moaipay.R


import com.dreamstep.moaipay.fragment.dummy.dummy.DummyContent
import com.dreamstep.moaipay.utils.BaseAdapter
import com.dreamstep.moaipay.utils.BaseViewHolder
import com.dreamstep.moaipay.utils.ViewUtils

import kotlinx.android.synthetic.main.fragment_dummy.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class DummyRecyclerViewAdapter(
    val listener: DummyPresenter.DummyListCallback
) : BaseAdapter<DummyContent.DummyData>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_dummy, parent, false)
        )
    }

    // VIEW HOLDERS
    // ====================================================
    inner class ViewHolder(itemView: View): BaseViewHolder(itemView) {

        // ====================================================
        override fun onBind(position: Int) {
            super.onBind(position)

            // Object ========> POST
            val dummyData = mItemList[position]

            resetPostView()

            printNormalPost(position, dummyData)
        }

        private fun resetPostView (){
            ViewUtils.showView(itemView)
        }

        private fun printNormalPost(position: Int, data: DummyContent.DummyData){
            val id = position.toString()
            val name = data.name

            ViewUtils.putText(itemView.item_id, id)
            ViewUtils.putText(itemView.content, name)
        }

    }
}

