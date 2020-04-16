package com.dreamstep.moaipay.fragment.dummy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.fragment.dummy.dummy.DummyContent.DummyData
import kotlinx.android.synthetic.main.fragment_dummy_list.*

class DummyFragment : Fragment(), DummyPresenter.DummyListCallback {

    private lateinit var presenter: DummyPresenter
    private var adapter = DummyRecyclerViewAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dummy_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lstDummy.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        lstDummy.itemAnimator = DefaultItemAnimator()
        lstDummy.adapter = adapter

        presenter = DummyPresenter(context!!, this)
        presenter.getData()

        btnDummy.setOnClickListener {
            if (!txtDummy.text.isNullOrBlank()) {
                presenter.submitData(txtDummy.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        lstDummy?.adapter = null
        super.onDestroyView()
    }

    override fun renderDummyList(dummyList: ArrayList<DummyData>) {
        adapter.setItems(dummyList)
    }
}
