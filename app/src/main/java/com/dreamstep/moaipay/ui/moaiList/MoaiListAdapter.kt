package com.dreamstep.moaipay.ui.moaiList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.MoaiGroup
import com.dreamstep.moaipay.interfaces.callback.MoaiListCallback
import com.dreamstep.moaipay.utils.BaseAdapter
import com.dreamstep.moaipay.utils.BaseViewHolder
import com.dreamstep.moaipay.utils.ViewUtils
import kotlinx.android.synthetic.main.item_moai_list.view.*
import java.text.SimpleDateFormat

class MoaiListAdapter(
    val listener: MoaiListCallback
): BaseAdapter<MoaiGroup>() {

    lateinit var context: Context

    // FUNCTIONS
    // ====================================================
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_moai_list, parent, false)
        )
    }

    // VIEW HOLDERS
    // ====================================================
    inner class ViewHolder(itemView: View): BaseViewHolder(itemView) {

        // ====================================================
        override fun onBind(position: Int) {
            super.onBind(position)

            // Object ========> POST
            val moaiGroup = mItemList[position]

            resetPostView()

//            if(position == 0) {
//                ViewUtils.hideView(itemView)
//            } else {
//                ViewUtils.showView(itemView)
                printNormalPost(moaiGroup)
//            }

            // CLICKS LISTENERS
            // ====================================================
            itemView.setOnClickListener {
//                listener.onMustBeFollower(moaiGroup, true, false)
            }
        }

        private fun printGeneralPost(post: MoaiGroup) {
        }

        private fun resetPostView (){
            ViewUtils.showView(itemView)
        }

        @SuppressLint("SimpleDateFormat")
        private fun printNormalPost(moaiGroup: MoaiGroup){
            val nextDate = moaiGroup.nextDate.toDate()
            val dfDate = SimpleDateFormat("yyyy年MM月dd日")
            val dfTime = SimpleDateFormat("HH:mm")
            val weekdays: Array<String> = arrayOf("日", "月", "火", "水", "木", "金", "土")
            val calendar = Calendar.getInstance()
            calendar.time = nextDate

            val date = dfDate.format(nextDate)
            val time = dfTime.format(nextDate)
            val weekday = weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]

            ViewUtils.putText(itemView.lblDate, date)
            ViewUtils.putText(itemView.lblWeek, weekday)
            ViewUtils.putText(itemView.lblTime, time)

            if (moaiGroup.name.length ?: 0 > 15) {
                val text = moaiGroup.name.substring(0, 15) + "..."
                ViewUtils.putText(itemView.lblMoaiName, text)
            } else {
                ViewUtils.putText(itemView.lblMoaiName, moaiGroup.name)
            }

            if (moaiGroup.nextLocation != "") {
                ViewUtils.putText(itemView.lblLocation, moaiGroup.nextLocation)
            }

            if (moaiGroup.nextLocation != "") {
                ViewUtils.putText(itemView.lblLocation, moaiGroup.nextLocation)
            }

            ViewUtils.renderImage(moaiGroup.imageUrl, itemView.imageMoai, context)

            if (moaiGroup.nextUrl != "") {
                itemView.lblLocation.setOnClickListener {
                    val uri = Uri.parse(moaiGroup.nextUrl)
                    val i = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(i);
                }
            }
        }

    }
}

