package com.dreamstep.moaipay.data.presenter

import android.content.Context
import com.dreamstep.moaipay.data.model.Users
import com.dreamstep.moaipay.interfaces.callback.LoginCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class LoginPresenter(
    private val context: Context,
    private val mView: LoginCallback,
    private val mUserId: String
) {

    private val firebaseDB = FirebaseFirestore.getInstance()
    var usersListener: ListenerRegistration? = null

    fun checkFirebaseLogin() {
        val queryAccount = firebaseDB.collection("users")
            .whereEqualTo("userId", mUserId)
        usersListener = queryAccount.addSnapshotListener { result, e ->
            if (e == null) {
                if (result == null) {
                    return@addSnapshotListener
                }
                if (result.documentChanges.count() < 1) {
                    return@addSnapshotListener
                }

                val docChange = result.documentChanges.first()
                val user = docChange.document.toObject(Users::class.java)
                user.key = docChange.document.reference
                mView.loggedIn(user)

            } else {
                mView.loginError(null, e)
            }
        }

    }
}