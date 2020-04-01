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

enum class ChangeType(var num: Int) {
    RETURN(0),
    NEXT(1),
    PARENT(2);

    fun toInt(): Int {
        return this.num
    }

    companion object {
        fun fromInt(index: Int): ChangeType {
            return values().firstOrNull { it.num == index } ?: RETURN
        }
    }
}

data class MoaiGroup (
    var key: DocumentReference? = null,
    var name: String = "",
    var adminUserId: String = "",
    var adminUserName: String = "",
    var subAdmin: ArrayList<String>? = null,
    var imageUrl: String = "",
    var amount: Int = 0,
    var extra: Int = 0,
    var paymentType: Int = PaymentType.SPLIT.toInt(),
    var attendPrice: Int = 0,
    var absentPrice: Int = 0,
    var changeType: Int = ChangeType.RETURN.toInt(),
    var openInOutBook: Boolean = false,
    var openAttendBook: Boolean = false,
    var members: ArrayList<String> = ArrayList(),
    var nextDate: Timestamp = Timestamp(Date(2999, 12, 31)),
    var nextLocation: String = "",
    var nextUrl: String = "",
    var createDate: Timestamp = Timestamp.now()
)
