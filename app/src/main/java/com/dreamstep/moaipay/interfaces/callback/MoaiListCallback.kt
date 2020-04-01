package com.dreamstep.moaipay.interfaces.callback

import com.dreamstep.moaipay.data.model.MoaiGroup

interface MoaiListCallback {
    fun renderMoaiList(moaiList: ArrayList<MoaiGroup>)
}
