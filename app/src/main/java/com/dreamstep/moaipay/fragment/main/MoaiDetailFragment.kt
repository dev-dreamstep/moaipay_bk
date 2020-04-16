package com.dreamstep.moaipay.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.utils.BaseFragment

class MoaiDetailFragment : BaseFragment() {

    companion object {
        fun newInstance() = MoaiDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moai_detail, container, false)
    }
}
