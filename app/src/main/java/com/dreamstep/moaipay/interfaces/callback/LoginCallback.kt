package com.dreamstep.moaipay.interfaces.callback

import com.dreamstep.moaipay.data.model.Users
import java.lang.Exception

interface LoginCallback {
    fun loggedIn(user: Users)
    fun loginError(message: String?, e: Exception?)
}
