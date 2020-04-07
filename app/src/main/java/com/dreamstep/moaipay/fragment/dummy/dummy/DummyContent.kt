package com.dreamstep.moaipay.fragment.dummy.dummy

import com.google.firebase.firestore.DocumentReference

object DummyContent {
    data class DummyData(
        var key: DocumentReference? = null,
        var id: String = "",
        var name: String = ""
    )
}
