package com.dreamstep.moaipay.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class ChatMessage (
    var key: DocumentReference? = null,
    val moaiId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderAvatarUrl: String = "",
    val chatType: Int = 0,
    val message: String = "",
    val createDate: Timestamp = Timestamp.now()
)
