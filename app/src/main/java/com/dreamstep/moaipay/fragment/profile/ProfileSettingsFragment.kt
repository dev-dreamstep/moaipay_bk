package com.dreamstep.moaipay.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.utils.BaseFragment

class ProfileSettingsFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile_settings, container, false)

        return view
    }

}
