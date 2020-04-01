package com.dreamstep.moaipay.utils

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    // VARIABLES
    // ====================================================
    protected var mItemList: MutableList<T> = ArrayList()


    // FUNCTIONS
    // ====================================================
    // => ItemList
    open fun setItems(items: List<T>) {
        mItemList = ArrayList(items)
        notifyDataSetChanged()
    }
    open fun addItems(items: List<T>) {
        mItemList.addAll(items)
        notifyDataSetChanged()
    }
    open fun addItem(item: T) {
        mItemList.add(item)
        notifyDataSetChanged()
    }
    open fun clearItems() {
        mItemList.clear()
        notifyDataSetChanged()
    }

    // => ViewHolder
    override fun getItemCount(): Int {
        return mItemList.size
    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

}