package com.dreamstep.moaipay.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.*
import kotlin.collections.ArrayList

enum class PaymentType(var num: Int) {
    SPLIT(0),
    DUES(1),
    PARENT(2);

    fun toInt(): Int {
        return this.num
    }

    companion object {
        fun fromInt(index: Int): PaymentType {
            return values().firstOrNull { it.num == index } ?: SPLIT
        }
    }
}

data class MoaiGroup (
    var key: DocumentReference? = null,
    var name: String = "",
    var imageUrl: String = "",
    var adminUserId: String = "",
    var adminUserName: String = "",
    var subAdmin: ArrayList<String>? = null,
    var amount: Int = 0,
    var extra: Int = 0,
    var paymentType: Int = PaymentType.SPLIT.toInt(),
    var attendPrice: Int = 0,
    var absentPrice: Int = 0,
    var changeSum: Int = 0,
    var members: ArrayList<String> = ArrayList(),
    var recievedMembers: ArrayList<String> = ArrayList(),
    var nextDate: Timestamp = Timestamp(Date(2999, 12, 31)),
    var nextLocation: String = "",
    var nextUrl: String = "",
    var createDate: Timestamp = Timestamp.now()
)
