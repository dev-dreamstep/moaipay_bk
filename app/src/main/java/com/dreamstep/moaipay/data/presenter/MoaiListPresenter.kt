package com.dreamstep.moaipay.data.presenter

import android.content.Context
import com.dreamstep.moaipay.data.model.MoaiGroup
import com.dreamstep.moaipay.interfaces.callback.MoaiListCallback
import com.dreamstep.moaipay.utils.MoaiPayGlobal
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MoaiListPresenter(
    private val context: Context,
    private val mView: MoaiListCallback
) {

    private val firebaseDB = FirebaseFirestore.getInstance()
    var moaiListListener: ListenerRegistration? = null

    /*
     * 所属している模合を検索
     */
    fun fetchMoaiGroup() {
        val queryAccount = firebaseDB.collection("moai")
            .whereArrayContains("members", MoaiPayGlobal.User!!.key!!.id)
        moaiListListener = queryAccount.addSnapshotListener { result, e ->
            if (e == null) {
                if (result == null) {
                    return@addSnapshotListener
                }
                if (result.documentChanges.count() < 1) {
                    return@addSnapshotListener
                }

                val moaiList = ArrayList<MoaiGroup>()
                result.documentChanges.forEach { docChange ->
                    val group = docChange.document.toObject(MoaiGroup::class.java)
                    group.key = docChange.document.reference
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                    moaiList.add(group)
                }
                val sortedList = ArrayList(moaiList.sortedByDescending { it.nextDate })
                mView.renderMoaiList(sortedList)
            }
        }

    }

}