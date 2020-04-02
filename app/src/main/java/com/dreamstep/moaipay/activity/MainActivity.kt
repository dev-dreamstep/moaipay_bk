package com.dreamstep.moaipay.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.Users
import com.dreamstep.moaipay.fragment.main.MoaiFragment
import com.dreamstep.moaipay.fragment.main.dummy.DummyContent
import com.dreamstep.moaipay.fragment.moaiList.MoaiListFragment
import com.dreamstep.moaipay.ui.main.SectionsPagerAdapter
import com.dreamstep.moaipay.utils.MoaiPayGlobal
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), MoaiFragment.OnListFragmentInteractionListener {

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

//        if (mAuth.currentUser == null) {
//            mAuth.currentUser?.let {
//                val intent = Intent(this, LoginActivity::class.java)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//            }
//        } else {
//            MoaiPayGlobal.AuthUserId = mAuth.currentUser!!.uid
//        }
        // 強制ログイン
        MoaiPayGlobal.AuthUserId = "LQsQ02mSGqXPLormkljSmJI5gJ13"


    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }
}