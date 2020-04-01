package com.dreamstep.moaipay.utils

import android.app.Application
import com.dreamstep.moaipay.data.model.Users

class MoaiPayGlobal: Application() {

    companion object {
        var AuthUserId: String = ""
        var User: Users? = null
    }
}
