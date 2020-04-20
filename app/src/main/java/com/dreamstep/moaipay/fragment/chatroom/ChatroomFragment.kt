package com.dreamstep.moaipay.fragment.chatroom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.activity.MessengerActivity
import com.dreamstep.moaipay.fragment.ChatPresenter
import com.dreamstep.moaipay.fragment.chatroom.dummy.DummyContent
import com.dreamstep.moaipay.fragment.chatroom.dummy.DummyContent.DummyItem
import com.dreamstep.moaipay.fragment.dummy.DummyPresenter
import com.dreamstep.moaipay.fragment.dummy.DummyRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_chatroom.*
import kotlinx.android.synthetic.main.fragment_dummy_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ChatroomFragment.OnListFragmentInteractionListener] interface.
 */
class ChatroomFragment : Fragment(), DummyPresenter.DummyListCallback {

    private lateinit var presenter: DummyPresenter
    private var adapter = DummyRecyclerViewAdapter(this)

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chatroom, container, false)

        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = ChatroomRecyclerViewAdapter(DummyContent.ITEMS, listener)
//            }
//        }
        return view
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


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() =
            ChatroomFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }

    }

    override fun onDestroyView() {
        lstDummy?.adapter = null
        super.onDestroyView()
    }

    override fun renderDummyList(dummyList: ArrayList<com.dreamstep.moaipay.fragment.dummy.dummy.DummyContent.DummyData>) {
        adapter.setItems(dummyList)
    }
}

