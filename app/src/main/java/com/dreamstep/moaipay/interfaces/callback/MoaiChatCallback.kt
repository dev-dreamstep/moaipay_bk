package com.dreamstep.moaipay.interfaces.callback

import com.dreamstep.moaipay.data.model.ChatMessage

interface MoaiChatCallback {

    fun renderPostsList(postsList: ArrayList<ChatMessage>)

    fun onTexMessageSent()

//    fun onImageMessageSent()
//
//    fun onVideoMessageSent()
//
//    fun onVideoNotCompatible()
//
//    fun onVideoUploadError()

    // => View
    fun showLoading()

    fun hideLoading()

//    fun showSwipeLoading()
//
//    fun hideSwipeLoading()

    fun onError(error: String)

//    fun onImageUploadError()

}