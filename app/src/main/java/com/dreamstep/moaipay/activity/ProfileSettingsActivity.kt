package com.dreamstep.moaipay.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dreamstep.moaipay.R
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        btnSaveProfile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("startFragment", MainActivity.StartFragment.MOAI_START)
                .flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}
