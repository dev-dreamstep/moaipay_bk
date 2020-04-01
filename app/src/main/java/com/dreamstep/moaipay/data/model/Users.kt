package com.dreamstep.moaipay.data.model

import com.google.firebase.firestore.DocumentReference

data class Users (
    var key: DocumentReference? = null,
    val name: String = "",
    val avatar: String = "",
    var userId: String = "",
    var moaiInvite: ArrayList<MoaiInvite> = ArrayList()
)

data class MoaiInvite (
    var moaiId: String = "",
    var moaiName: String = "",
    var moaiImageUrl: String = "",
    var inviteeId: String = "",
    var inviteeName: String = "",
    var inviteeAvatar: String = ""
)
