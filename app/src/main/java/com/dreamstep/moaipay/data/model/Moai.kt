package com.dreamstep.moaipay.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.*

data class Moai (
    var key: DocumentReference? = null,
    var term: Int = 1,
    var createDate: Timestamp = Timestamp.now()
)
