package com.dreamstep.moaipay.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.utils.BaseFragment

class MoaiInfoFragment : BaseFragment() {

    companion object {
        fun newInstance() = MoaiInfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moai_info, container, false)
    }
}
