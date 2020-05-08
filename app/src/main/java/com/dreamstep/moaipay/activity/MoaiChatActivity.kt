package com.dreamstep.moaipay.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.MoaiGroup
import com.dreamstep.moaipay.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*

class MoaiChatActivity : AppCompatActivity() {

    private lateinit var mMoaiGroup: MoaiGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moai_chat)

        mMoaiGroup = intent.extras!!.getParcelable(BUNDLE_MOAIGROUP)!!

        ViewUtils.putText(titleHeader, "もあい名")

    }

    // PUBLIC STATIC
    // ====================================================
    companion object {

        private const val BUNDLE_MOAIGROUP = "BUNDLE_MOAIGROUP"

        fun newMoaiChatIntent(context: Context, chatRoom: MoaiGroup): Intent {
            val intent = Intent(context, MoaiChatActivity::class.java)
            val bundle = Bundle()

            bundle.putParcelable(BUNDLE_MOAIGROUP, chatRoom)

            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            return intent
        }

    }
}
