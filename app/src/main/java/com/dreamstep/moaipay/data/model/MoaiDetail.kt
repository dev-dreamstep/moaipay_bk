package com.dreamstep.moaipay.data.model

import com.google.firebase.Timestamp

data class MoaiDetail(
    var numbers: Int = 1,
    var eventDate: Timestamp = Timestamp.now(),
    var dealerId: String = "",
    var behalfId: String = "",
    var locationName: String = "",
    var locationUrl: String = "",
    var locationText: String = "",
    var planAttend: ArrayList<String> = ArrayList(),
    var planAbsent: ArrayList<String> = ArrayList(),
    var check: Map<String, ArrayList<String>>
)

//data class DealerCheck(
//    var attend: ArrayList<String> = ArrayList(),
//    var recieveMoai: ArrayList<String> = ArrayList(),
//    var recievePayment: ArrayList<String> = ArrayList(),
//    var attend: ArrayList<String> = ArrayList(),
//    var attend: ArrayList<String> = ArrayList()
//)
