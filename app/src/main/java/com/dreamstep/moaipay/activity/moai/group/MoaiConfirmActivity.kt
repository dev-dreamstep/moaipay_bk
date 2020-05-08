package com.dreamstep.moaipay.activity.moai.group

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.activity.MainActivity
import com.dreamstep.moaipay.data.model.MoaiGroup
import kotlinx.android.synthetic.main.activity_moai_confirm.*

class MoaiConfirmActivity : AppCompatActivity() {

    companion object {
        const val MOAI_DATA = "MOAI_DATA"
    }

    lateinit var data: MoaiGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moai_confirm)

//        val json = intent.extras?.getString(MOAI_DATA)
//        data = Gson().fromJson<MoaiGroup>(json, MoaiGroup::class.java)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        btnBack.setOnClickListener { onBackPressed() }

        btnSaveMoai.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
 }
