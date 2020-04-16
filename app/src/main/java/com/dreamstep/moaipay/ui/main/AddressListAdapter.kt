package com.dreamstep.moaipay.ui.main

import android.content.Context
import android.view.View
import android.widget.TextView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.utils.AddressListHeader
import com.dreamstep.moaipay.utils.AddressListRow
import com.dreamstep.moaipay.utils.BaseSectionAdapter
import com.dreamstep.moaipay.utils.IndexPath


class AddressListAdapter(
    override var context: Context,
    override var sectionList: List<AddressListHeader?>,
    override var rowList: List<List<AddressListRow?>>
): BaseSectionAdapter<AddressListHeader?, AddressListRow?>(context, sectionList, rowList)
    {
        override fun viewForHeaderInSection(convertView: View?, section: Int): View {
            var convView: View? = convertView
            var holder: ListHeaderViewHolder? = null
            if (convView == null) {
                convView = inflater.inflate(R.layout.item_address_list_header, null)
                holder = ListHeaderViewHolder()
                holder.titleTxt = convView.findViewById(R.id.txtHeader)
                convView.setTag(holder)
            } else {
                holder = convView.getTag() as ListHeaderViewHolder?
            }
            val headerData: AddressListHeader = sectionList.get(section)!!
            holder!!.titleTxt?.setText(headerData.title)
            holder.subtitleTxt?.setText(headerData.subTitle)
            return convView!!
        }

        override fun cellForRowAtIndexPath(convertView: View?, indexPath: IndexPath): View {
            var convView: View? = convertView
            var holder: ListRowViewHolder? = null
            if (convView == null) {
                convView = inflater.inflate(R.layout.item_address_list_row, null)
                holder = ListRowViewHolder()
                holder.txtUserName = convView.findViewById(R.id.txtUserName)
                holder.txtUserTel = convView.findViewById(R.id.txtUserTel)
                convertView!!.setTag(holder)
            } else {
                holder = convView.getTag() as ListRowViewHolder?
            }
            val rowData: AddressListRow = rowList.get(indexPath.section).get(indexPath.row)!!
            holder!!.txtUserName!!.setText(rowData.userName)
            holder.txtUserTel!!.setText(rowData.userTel.toString())
            return convView!!
        }

        internal class ListHeaderViewHolder {
            var titleTxt: TextView? = null
            var subtitleTxt: TextView? = null
        }

        internal class ListRowViewHolder {
            var txtUserName: TextView? = null
            var txtUserTel: TextView? = null
        }
    }
