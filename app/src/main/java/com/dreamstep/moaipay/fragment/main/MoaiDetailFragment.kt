package com.dreamstep.moaipay.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.Members
import com.dreamstep.moaipay.data.model.MoaiDetail
import com.dreamstep.moaipay.ui.main.MoaiDetailAdapter
import com.dreamstep.moaipay.ui.main.MoaiDetailColHeaderAdapter
import com.dreamstep.moaipay.ui.main.MoaiDetailRowHeaderAdapter
import com.dreamstep.moaipay.utils.BaseFragment
import com.google.firebase.Timestamp

class MoaiDetailFragment : BaseFragment() {

    companion object {
        fun newInstance() = MoaiDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moai_detail, container, false)

        setRecyclerView(view)

        return view
    }

    private fun setRecyclerView(view: View) {
        val listColHeader = view.findViewById<RecyclerView>(R.id.recyclerColHeader)
        val listRowHeader = view.findViewById<RecyclerView>(R.id.recyclerRowHeader)
        val listDetail = view.findViewById<RecyclerView>(R.id.recyclerDetail)
        val colHeaderAdapter = MoaiDetailColHeaderAdapter()
        val rowHeaderAdapter = MoaiDetailRowHeaderAdapter(ArrayList())
        val detailAdapter = MoaiDetailAdapter()

        listColHeader.layoutManager = GridLayoutManager(context, 4)
        listRowHeader.layoutManager = LinearLayoutManager(context)
        listDetail.layoutManager = GridLayoutManager(context, 4)

        listColHeader.adapter = colHeaderAdapter
        listRowHeader.adapter = rowHeaderAdapter
        listDetail.adapter = detailAdapter

        colHeaderAdapter.setItems(dummyDataMoaiDetail())
        rowHeaderAdapter.setItems(dummyDataMembers())
        detailAdapter.setItems(dummyDataMoaiDetail())
    }


    private fun dummyDataMoaiDetail(): List<MoaiDetail> {
        val data = ArrayList<MoaiDetail>()
        for (i in 0..9) {
            val mapCheck = mutableMapOf(
                "attend" to ArrayList<String>(),
                "amount" to ArrayList(),
                "price" to ArrayList(),
                "payment1" to ArrayList()
            )
            data.add(MoaiDetail(check = mapCheck))
        }

        return data
    }

    private fun dummyDataMembers(): List<Members> {
        val data = ArrayList<Members>()
        for (i in 0..9) {
            data.add(Members())
        }

        return data
    }

}
