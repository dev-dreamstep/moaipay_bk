package com.dreamstep.moaipay.utils

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

open class BaseSectionAdapter<T1, T2>(
    protected open var context: Context,
    /** ヘッダー行で使用するデータリスト */
    protected open var sectionList: List<T1>,
    /** データ行で使用するデータリスト */
    protected open var rowList: List<List<T2>>
) : BaseAdapter() {

    /** インデックス行:ヘッダー */
    private val INDEX_PATH_ROW_HEADER = -1

    /** ビュータイプ:ヘッダー行 */
    private val ITEM_VIEW_TYPE_HEADER = 0

    /** ビュータイプ:データ行 */
    private val ITEM_VIEW_TYPE_ROW = 1

    protected var inflater: LayoutInflater = LayoutInflater.from(context)
    private var indexPathList: List<IndexPath>

    init {
        this.indexPathList = getIndexPathList(sectionList, rowList)
    }

    override fun getCount(): Int {
        return indexPathList.count()
    }

    override fun getItem(position: Int): Any {
        val indexPath = indexPathList[position]
        if (isHeader(indexPath)) {
            return sectionList[indexPath.section]!!
        } else {
            return rowList[indexPath.section][indexPath.row]!!
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val indexPath = indexPathList[position]

        // ヘッダー行とデータ行とで分岐します。
        if (isHeader(indexPath)) {
            return viewForHeaderInSection(convertView, indexPath.section)
        } else {
            return cellForRowAtIndexPath(convertView, indexPath)
        }
    }

    /**
     * ヘッダー行のViewを返します。
     *
     * @param convertView
     * @param section
     * @return ヘッダー行のView
     */
    open fun viewForHeaderInSection(convertView: View?, section: Int): View {
        var convView = convertView
        if (convView == null) {
            convView = inflater.inflate(android.R.layout.simple_list_item_1, null)
            val castedConvertView = convView as TextView
            castedConvertView.setBackgroundColor(Color.GRAY)
            castedConvertView.setTextColor(Color.WHITE)
        }
        val textView = convView as TextView
        ViewUtils.putText(textView, sectionList[section].toString())
        return convView
    }

    /**
     * データ行のViewを返します。
     *
     * @param convertView
     * @param indexPath
     * @return データ行のView
     */
    open fun cellForRowAtIndexPath(convertView: View?, indexPath: IndexPath): View {
        var convView = convertView
        if (convView == null) {
            convView = inflater.inflate(android.R.layout.simple_list_item_1, null)
        }
        val textView = convertView as TextView
        ViewUtils.putText(textView, rowList[indexPath.section][indexPath.row].toString())
        return convView!!
    }

    override fun getViewTypeCount(): Int {
        // ヘッダー行とデータ行の2種類なので、2を返します。
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        // ビュータイプを返します。
        if (isHeader(position)) {
            return ITEM_VIEW_TYPE_HEADER
        } else {
            return ITEM_VIEW_TYPE_ROW
        }
    }

    override fun isEnabled(position: Int): Boolean {
        if (isHeader(position)) {
            // ヘッダー行の場合は、タップできないようにします。
            return false
        } else {
            return super.isEnabled(position)
        }
    }

    /**
     * インデックスパスリストを取得します。
     *
     * @param sectionList
     * @param rowList
     * @return インデックスパスリスト
     */
    private fun getIndexPathList(sectionList: List<T1>, rowList: List<List<T2>>): List<IndexPath> {
        val indexPathList = ArrayList<IndexPath>()
        for (i in 0 until sectionList.count()) {
        val sectionIndexPath = IndexPath()
        sectionIndexPath.section = i
        sectionIndexPath.row = INDEX_PATH_ROW_HEADER
        indexPathList.add(sectionIndexPath)

        val rowListBySection = rowList[i]
        for (j in 0 until rowListBySection.count()) {
            val rowIndexPath = IndexPath()
            rowIndexPath.section = i
            rowIndexPath.row = j
            indexPathList.add(rowIndexPath)
        }
    }
        return indexPathList
    }

    private fun isHeader(position: Int): Boolean {
        val indexPath = indexPathList[position]
        return isHeader(indexPath)
    }

    private fun isHeader(indexPath: IndexPath): Boolean {
        return INDEX_PATH_ROW_HEADER == indexPath.row
    }

}
