package com.dreamstep.moaipay.fragment.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.ui.main.MoaiAttendanceListAdapter
import com.dreamstep.moaipay.ui.main.MoaiDetailPagerAdapter
import com.dreamstep.moaipay.ui.main.PAGE_INDEX_MOAI_DETAIL
import com.dreamstep.moaipay.ui.main.PAGE_INDEX_MOAI_INFOMATION
import com.dreamstep.moaipay.utils.BaseFragment
import com.dreamstep.moaipay.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_moai_detail.*
import kotlinx.android.synthetic.main.layout_moai_detail.view.*
import kotlinx.android.synthetic.main.layout_tab_moai_detail.view.*

class MoaiFragment : BaseFragment() {

    lateinit var mMoaiPager: ViewPager
    lateinit var mTabDetail: View
    lateinit var mTabInfomation: View
    lateinit var mLayoutMore: View
    lateinit var mLayoutPopup: View

    companion object {
        fun newInstance() = MoaiFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        setupViewPager(view)
        setupTabs(view)

        setRecyclerView(view)

        initVisible(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        btnMore.setOnClickListener {
            if (layoutMore.visibility != View.VISIBLE) {
                layoutMore.visibility = View.VISIBLE
            } else {
                layoutMore.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView(view: View) {
        val listAttend = view.findViewById<RecyclerView>(R.id.lstAttend)
        val listAbsent = view.findViewById<RecyclerView>(R.id.lstAbsent)
        val listNotAns = view.findViewById<RecyclerView>(R.id.lstNotAns)
        val attendAdapter = MoaiAttendanceListAdapter()
        val absentAdapter = MoaiAttendanceListAdapter()
        val notAnsAdapter = MoaiAttendanceListAdapter()

        listAttend.layoutManager = GridLayoutManager(context, 4)
        listAbsent.layoutManager = GridLayoutManager(context, 4)
        listNotAns.layoutManager = GridLayoutManager(context, 4)

        listAttend.adapter = attendAdapter
        listAbsent.adapter = absentAdapter
        listNotAns.adapter = notAnsAdapter

    }

    private fun setupViewPager(view: View) {

        mMoaiPager = view.findViewById(R.id.moaiPager)
        mMoaiPager.adapter = MoaiDetailPagerAdapter(activity!!.supportFragmentManager)

        mMoaiPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                changeCurrentPage(position, false)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setupTabs(view: View) {
        mTabDetail = view.findViewById(R.id.tabDetail)
        mTabInfomation = view.findViewById(R.id.tabInfomation)

        mTabDetail.lblTabName.setTextColor(Color.parseColor(getString(R.color.colorMoaiText)))
        mTabDetail.lineTab.setBackgroundColor(Color.parseColor(getString(R.color.colorBeerDark)))
        ViewUtils.putText(mTabDetail.lblTabName, "参加情報")
        mTabInfomation.lblTabName.setTextColor(Color.parseColor(getString(R.color.colorMoaiGray)))
        mTabInfomation.lineTab.setBackgroundColor(Color.parseColor(getString(R.color.colorMoaiGray)))
        ViewUtils.putText(mTabInfomation.lblTabName, "開催情報")

        changeCurrentPage(PAGE_INDEX_MOAI_DETAIL)

        mTabDetail.setOnClickListener {
            changeCurrentPage(PAGE_INDEX_MOAI_DETAIL)
        }
        mTabInfomation.setOnClickListener {
            changeCurrentPage(PAGE_INDEX_MOAI_INFOMATION)
        }
    }

    private fun changeCurrentPage(index: Int, changePage: Boolean = true) {

        when (index) {
            PAGE_INDEX_MOAI_DETAIL -> {
                updateTabColor(index, mTabDetail, true)
                updateTabColor(index, mTabInfomation, false)
            }
            PAGE_INDEX_MOAI_INFOMATION -> {
                updateTabColor(index, mTabDetail, false)
                updateTabColor(index, mTabInfomation, true)
            }
        }

        if (changePage) {
            mMoaiPager.currentItem = index
        }
    }

    private fun updateTabColor(index: Int, tab: View, selected: Boolean) {
        val label = tab.findViewById<TextView>(R.id.lblTabName)
        val line = tab.findViewById<View>(R.id.lineTab)

        if (selected) {
            label.setTextColor(Color.parseColor(getString(R.color.colorMoaiText)))
            line.setBackgroundColor(Color.parseColor(getString(R.color.colorBeerDark)))
        } else {
            label.setTextColor(Color.parseColor(getString(R.color.colorMoaiGray)))
            line.setBackgroundColor(Color.parseColor(getString(R.color.colorMoaiGray)))
        }
    }

    private fun initVisible(view: View) {
        mLayoutMore = view.findViewById(R.id.layoutMore)
        mLayoutPopup = view.findViewById(R.id.layoutPopup)
        mLayoutMore.visibility = View.GONE
        mLayoutPopup.visibility = View.GONE
    }
}
