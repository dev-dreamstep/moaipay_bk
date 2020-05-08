package com.dreamstep.moaipay.activity.moai.group

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.PopupWindow
import com.dreamstep.moaipay.R
import kotlinx.android.synthetic.main.activity_moai_regist.*

class MoaiRegistActivity : AppCompatActivity() {

    lateinit var mPopupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moai_regist)

        mPopupWindow = PopupWindow(this)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        radioPayType1.setOnCheckedChangeListener { radio: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                radioPayType2.isChecked = false
                radioPayType3.isChecked = false
            }
        }
        radioPayType2.setOnCheckedChangeListener { radio: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                radioPayType1.isChecked = false
                radioPayType3.isChecked = false
            }
        }
        radioPayType3.setOnCheckedChangeListener { radio: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                radioPayType1.isChecked = false
                radioPayType2.isChecked = false
            }
        }

        btnBack.setOnClickListener { onBackPressed() }

        btnConfirm.setOnClickListener {
//            val moaiGroup = MoaiGroup()
//            val json = Gson().toJson(moaiGroup)
            val intent = Intent(this, MoaiConfirmActivity::class.java)
//            intent.putExtra(MOAI_DATA, json)
            startActivity(intent)
        }

        btnInviteMember.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.popup_tel_list, null)
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
            mPopupWindow.showAtLocation(barHeader, Gravity.NO_GRAVITY, 0, 0)
        }
    }
 }
