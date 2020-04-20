package com.dreamstep.moaipay.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.ui.main.MoaiAttendanceListAdapter
import com.dreamstep.moaipay.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MoaiFragment : BaseFragment() {

    companion object {
        fun newInstance() = MoaiFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        setRecyclerView(view)

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
}
