package com.dreamstep.moaipay.activity

import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.Users
import com.dreamstep.moaipay.data.presenter.LoginPresenter
import com.dreamstep.moaipay.interfaces.callback.LoginCallback
import com.dreamstep.moaipay.utils.MoaiPayGlobal
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity(), LoginCallback {

    private var presenter: LoginPresenter? = null

    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    private var mSMSVerificationID = ""
    private var phoneUtil = PhoneNumberUtil.getInstance()
    private var formattedNumber: Phonenumber.PhoneNumber? = null
    private var formatted: String? = null

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(this@LoginActivity, ": Verification Completed", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@LoginActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                mSMSVerificationID = p0
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                mSMSVerificationID = p0
            }
        }

        username.afterTextChanged {
            phoneVerify.isEnabled = !username.text.isNullOrEmpty()

            phoneVerify.setOnClickListener {
                try {
                    val manager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                    val countryCode = manager.networkCountryIso
                    formattedNumber = phoneUtil.parse(username.text.toString(), countryCode.toUpperCase(Locale.getDefault()))
                    formatted = phoneUtil.format(formattedNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
                    registerPhoneNumber(formatted!!)
                } catch (e: NumberParseException) {
                    Toast.makeText(this@LoginActivity, formatted, Toast.LENGTH_SHORT).show()
                }
            }
        }

        password.apply {
            afterTextChanged {
                login.isEnabled = !password.text.isNullOrEmpty()
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE

                val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
                val networkInfo = connectionManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    val credential = PhoneAuthProvider.getCredential(
                        mSMSVerificationID,
                        password.text.toString()
                    )
                    mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mAuth.currentUser?.let {
                                presenter = LoginPresenter(
                                    this@LoginActivity,
                                    this@LoginActivity,
                                    it.uid
                                )
                                presenter!!.checkFirebaseLogin()
                            }
                        } else {
                            Log.w("Phone", "signInWithPhoneAuthCredential:failure", task.exception)
                            Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, context.getString(R.string.cannot_connect), Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (MoaiPayGlobal.User != null) {
            val intent = Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun loggedIn(user: Users) {
        MoaiPayGlobal.User = user

        if (presenter!!.usersListener != null) {
            presenter!!.usersListener!!.remove()
        }

        val intent = Intent(this, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

        loading.visibility = View.INVISIBLE
    }

    override fun loginError(message: String?, e: Exception?) {
        if (presenter!!.usersListener != null) {
            presenter!!.usersListener!!.remove()
        }

        password.setText("")
        when {
            !message.isNullOrBlank() -> Log.e("LoginActivity", message)
            e != null -> Log.e("LoginActivity", e.message!!)
            else -> Log.e("LoginActivity", "ERROR")
        }
        loading.visibility = View.INVISIBLE
    }

    private fun registerPhoneNumber(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            10,
            TimeUnit.SECONDS,
            this@LoginActivity,
            mCallbacks!!
        )
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
