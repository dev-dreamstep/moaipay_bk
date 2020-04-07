package com.dreamstep.moaipay.fragment.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dreamstep.moaipay.R
import kotlinx.android.synthetic.main.fragment_start_tutorial.*

private const val ARG_POSITION = "position"

class StartTutorialFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTutorial()
    }

    private fun setupTutorial() {

        when (position) {
            0 -> back_iv.setImageResource(R.mipmap.moai)
            1 -> back_iv.setImageResource(R.mipmap.moai2)
            else -> back_iv.setImageResource(R.mipmap.moai3)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            StartTutorialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }
}
