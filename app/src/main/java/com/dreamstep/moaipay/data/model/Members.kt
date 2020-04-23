package com.dreamstep.moaipay.data.model

import com.google.firebase.firestore.DocumentReference

data class Members (
    var key: DocumentReference? = null,
    var userId: String = "",    // documentId
    val name: String = "",
    val avatar: String = ""
)
