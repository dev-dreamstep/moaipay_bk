package com.dreamstep.moaipay.data.model

import android.os.Parcel
import android.os.Parcelable
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
    var key: DocumentReference?,
    var name: String,
    var imageUrl: String,
    var adminUserId: String,
    var adminUserName: String,
    var subAdmin: ArrayList<String>?,
    var amount: Int,
    var extra: Int,
    var paymentType: Int,
    var attendPrice: Int,
    var absentPrice: Int,
    var changeSum: Int,
    var members: ArrayList<String>,
    var recievedMembers: ArrayList<String>,
    var nextDate: Timestamp,
    var nextLocation: String,
    var nextUrl: String,
    var createDate: Timestamp
) : Parcelable {
    constructor(parcel: Parcel) : this(
        null,
        "",
        "",
        "",
        "",
        null,
        0,
        0,
        PaymentType.SPLIT.toInt(),
        0,
        0,
        0,
        ArrayList(),
        ArrayList(),
        Timestamp(Date(2999, 12, 31)),
        "",
        "",
        Timestamp.now()
    ) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoaiGroup> {
        override fun createFromParcel(parcel: Parcel): MoaiGroup {
            return MoaiGroup(parcel)
        }

        override fun newArray(size: Int): Array<MoaiGroup?> {
            return arrayOfNulls(size)
        }
    }
}
