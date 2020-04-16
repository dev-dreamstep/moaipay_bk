package com.dreamstep.moaipay.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.fragment.main.dummy.DummyContent.DummyItem

class MoaiRecyclerViewAdapter(
    private val mValues: List<DummyItem>
) : RecyclerView.Adapter<MoaiRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_moai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        holder.mIdView.text = item.id
//        holder.mContentView.text = item.content
//        holder.mContentView2.text = item.content
//        holder.mContentView3.text = item.content
//        holder.mContentView4.text = item.details
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        val mIdView: TextView = mView.item_number
//        val mContentView: TextView = mView.content
//        val mContentView2: TextView = mView.content2
//        val mContentView3: TextView = mView.content3
//        val mContentView4: TextView = mView.content4

        override fun toString(): String {
            return super.toString() + " '" //+ mContentView.text + "'"
        }
    }
}
