package com.dreamstep.moaipay.fragment.dummy

import android.content.Context
import com.dreamstep.moaipay.fragment.dummy.dummy.DummyContent
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DummyPresenter(
    private val context: Context,
    private val mView: DummyListCallback
) {
    private val firebaseDummyURL: String
        get() = "dummy"

    private val dummyHashMapTemplate
        get() = hashMapOf<String, Any>(
            "name" to ""
        )

    private val firebaseDB = FirebaseFirestore.getInstance()
    var dummyListener: ListenerRegistration? = null
    val list = ArrayList<DummyContent.DummyData>()

    interface DummyListCallback {
        fun renderDummyList(dummyList: ArrayList<DummyContent.DummyData>)
    }

    fun getData() {
        val queryAccount = firebaseDB.collection("dummy")

        dummyListener?.remove()
        dummyListener = queryAccount.addSnapshotListener { result, e ->
            if (e == null) {
                if (result == null) {
                    return@addSnapshotListener
                }
                if (result.documentChanges.count() < 1) {
                    return@addSnapshotListener
                }

                result.documentChanges.forEach { docChange ->
                    val group = docChange.document.toObject(DummyContent.DummyData::class.java)
                    group.key = docChange.document.reference
                    group.id = group.key!!.id
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> list.add(group)
                        DocumentChange.Type.REMOVED -> list.remove(group)
                        DocumentChange.Type.MODIFIED -> {
                            val mod = list.filter { it.key == group.key }
                            list.remove(mod.first())
                            list.add(group)
                        }
                    }
                }
                val sortedList = ArrayList(list.sortedByDescending { it.name })
                mView.renderDummyList(sortedList)
            }
        }
    }

    fun submitData(text: String){

        val dummyHashMap = HashMap<String, Any>(dummyHashMapTemplate)
        dummyHashMap["name"] = text

        firebaseDB.collection(firebaseDummyURL).document().set(dummyHashMap)

//        mView.onTexMessageSent()

    }
}
