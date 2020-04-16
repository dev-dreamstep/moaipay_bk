package com.dreamstep.moaipay.fragment.main

import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.dreamstep.moaipay.R
import kotlinx.android.synthetic.main.fragment_moai_start.*


class MoaiStartFragment : Fragment() {

    lateinit var mView: View
    lateinit var mPopupWindow: PopupWindow

    companion object {
        fun newInstance() = MoaiStartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_moai_start, container, false)
        mPopupWindow = PopupWindow(mView)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // もあい作成ポップアップ
        btnCreate.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.popup_tutorial_moai_create, null)
            val buttonClose = popupView.findViewById<View>(R.id.btnClose)
            buttonClose.setOnClickListener {
                if (mPopupWindow.isShowing) {
                    mPopupWindow.dismiss()
                }
            }
            mPopupWindow.contentView = popupView
            mPopupWindow.setBackgroundDrawable(resources.getDrawable((R.drawable.popup_background)))
            mPopupWindow.setOutsideTouchable(true)
            mPopupWindow.setFocusable(true)
            mPopupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
            mPopupWindow.height = WindowManager.LayoutParams.MATCH_PARENT
            mPopupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0)
        }

        // もあい参加ポップアップ
        btnJoin.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.popup_tutorial_moai_join, null)
            val buttonClose = popupView.findViewById<View>(R.id.btnClose)
            buttonClose.setOnClickListener {
                if (mPopupWindow.isShowing) {
                    mPopupWindow.dismiss()
                }
            }
            mPopupWindow.contentView = popupView
            mPopupWindow.setBackgroundDrawable(resources.getDrawable((R.drawable.popup_background)))
            mPopupWindow.setOutsideTouchable(true)
            mPopupWindow.setFocusable(true)
            mPopupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
            mPopupWindow.height = WindowManager.LayoutParams.MATCH_PARENT
            mPopupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0)
        }

    }
}
