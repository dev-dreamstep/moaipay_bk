package com.dreamstep.moaipay.fragment.chatroom.view
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

import com.dreamstep.moaipay.fragment.chatroom.model.Attribute
import com.dreamstep.moaipay.fragment.chatroom.model.Message
import com.dreamstep.moaipay.fragment.chatroom.util.MessageDateComparator
import com.dreamstep.moaipay.fragment.chatroom.util.TimeUtils
import com.dreamstep.moaipay.fragment.dummy.DummyPresenter
import com.dreamstep.moaipay.fragment.dummy.dummy.DummyContent
import com.dreamstep.moaipay.utils.MoaiPayGlobal
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.message_view_left.view.*

import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

/**
 * Simple chat view
 * Created by nakayama on 2016/08/08.
 */
class MessageView : ListView, View.OnFocusChangeListener {

    /**
     * All contents such as right message, left message, date label
     */
    private var chatList: MutableList<Any> = ArrayList()
    /**
     * Only messages
     */
    val messageList = ArrayList<Message>()

    private lateinit var messageAdapter: MessageAdapter

    private lateinit var keyboardAppearListener: OnKeyboardAppearListener

    /**
     * MessageView is refreshed at this time
     */
    private var refreshInterval: Long = 60000

    private var attribute: Attribute

    interface OnKeyboardAppearListener {
        fun onKeyboardAppeared(hasChanged: Boolean)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        attribute = Attribute(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attribute = Attribute(context, attrs)
        init()
    }


    fun init(list: List<Message>) {
        chatList = ArrayList()
        choiceMode = ListView.CHOICE_MODE_NONE

        for (i in list.indices) {
            addMessage(list[i])
        }
        refresh()
        init()
    }

    fun init(attribute: Attribute) {
        this.attribute = attribute
        init()
    }

    /**
     * Initialize list
     */
    fun init() {
        dividerHeight = 0
        messageAdapter = MessageAdapter(context, 0, chatList, attribute)

        adapter = messageAdapter

        val weakMessageAdapter: WeakReference<MessageAdapter> = WeakReference(messageAdapter)
        val handler = Handler()
        val refreshTimer = Timer(true)
        refreshTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { weakMessageAdapter.get()?.notifyDataSetChanged() }
            }
        }, 1000, refreshInterval)
    }

    /**
     * Set new message and refresh
     * @param message new message
     */
    fun setMessage(message: Message) {
        addMessage(message)
        refresh()
        messageAdapter.notifyDataSetChanged()
    }

    /**
    * Dynamically update message status and refresh, updating the status icon
    * @param message message to update
    * @param status new status to be applied
    */
    fun updateMessageStatus(message: Message, status: Int)
    {
        val indexOfMessage = messageList.indexOf(message)
        val messageToUpdate = messageList[indexOfMessage]
        messageToUpdate.status = status
        messageAdapter.notifyDataSetChanged()
    }

    /**
     * Add message to chat list and message list.
     * Set date text before set message if sent at the different day.
     * @param message new message
     */
    private fun addMessage(message: Message) {
        messageList.add(message)
        if (messageList.size == 1) {
            chatList.add(message.dateSeparateText)
            chatList.add(message)
            return
        }
        val prevMessage = messageList[messageList.size - 2]
        if (!TimeUtils.isSameDay(prevMessage.sendTime, message.sendTime)) {
            chatList.add(message.dateSeparateText)
        }
        chatList.add(message)
    }

    private fun refresh() {
        sortMessages(messageList)
        chatList.clear()
        chatList.addAll(insertDateSeparator(messageList))
        messageAdapter.notifyDataSetChanged()
    }

    fun remove(message: Message) {
        messageList.remove(message)
        refresh()
    }

    fun removeAll() {
        messageList.clear()
        refresh()
    }

    private fun insertDateSeparator(list: List<Message>): List<Any> {
        val result = ArrayList<Any>()
        if (list.isEmpty()) {
            return result
        }
        result.add(list[0].dateSeparateText)
        result.add(list[0])
        if (list.size < 2) {
            return result
        }
        for (i in 1 until list.size) {
            val prevMessage = list[i - 1]
            val currMessage = list[i]
            if (!TimeUtils.isSameDay(prevMessage.sendTime, currMessage.sendTime)) {
                result.add(currMessage.dateSeparateText)
            }
            result.add(currMessage)
        }
        return result
    }

    /**
     * Sort messages
     */
    private fun sortMessages(list: List<Message>?) {
        val dateComparator = MessageDateComparator()
        if (list != null) {
            Collections.sort(list, dateComparator)
        }
    }

    fun setOnKeyboardAppearListener(listener: OnKeyboardAppearListener) {
        keyboardAppearListener = listener
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        // if ListView became smaller
        if (::keyboardAppearListener.isInitialized && height < oldHeight) {
            keyboardAppearListener.onKeyboardAppeared(true)
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            //Scroll to end
            scrollToEnd()
        }
    }

    fun scrollToEnd() {
        smoothScrollToPosition(count - 1)
    }

    fun setLeftBubbleColor(color: Int) {
        messageAdapter.setLeftBubbleColor(color)
    }

    fun setRightBubbleColor(color: Int) {
        messageAdapter.setRightBubbleColor(color)
    }

    fun setUsernameTextColor(color: Int) {
        messageAdapter.setUsernameTextColor(color)
    }

    fun setSendTimeTextColor(color: Int) {
        messageAdapter.setSendTimeTextColor(color)
    }

    fun setMessageStatusColor(color: Int) {
        messageAdapter.setStatusColor(color)
    }

    fun setDateSeparatorTextColor(color: Int) {
        messageAdapter.setDateSeparatorColor(color)
    }

    fun setRightMessageTextColor(color: Int) {
        messageAdapter.setRightMessageTextColor(color)
    }

    fun setLeftMessageTextColor(color: Int) {
        messageAdapter.setLeftMessageTextColor(color)
    }

    fun setOnBubbleClickListener(listener: Message.OnBubbleClickListener) {
        messageAdapter.setOnBubbleClickListener(listener)
    }

    fun setOnBubbleLongClickListener(listener: Message.OnBubbleLongClickListener) {
        messageAdapter.setOnBubbleLongClickListener(listener)
    }

    fun setOnIconClickListener(listener: Message.OnIconClickListener) {
        messageAdapter.setOnIconClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: Message.OnIconLongClickListener) {
        messageAdapter.setOnIconLongClickListener(listener)
    }

    fun setMessageMarginTop(px: Int) {
        messageAdapter.setMessageTopMargin(px)
    }

    fun setMessageMarginBottom(px: Int) {
        messageAdapter.setMessageBottomMargin(px)
    }

    fun setRefreshInterval(refreshInterval: Long) {
        this.refreshInterval = refreshInterval
    }

    fun setMessageFontSize(size: Float) {
        attribute.messageFontSize = size
        setAttribute()
    }

    fun setUsernameFontSize(size: Float) {
        attribute.usernameFontSize = size
        setAttribute()
    }

    fun setTimeLabelFontSize(size: Float) {
        attribute.timeLabelFontSize = size
        setAttribute()
    }

    fun setMessageMaxWidth(width: Int) {
        attribute.messageMaxWidth = width
        setAttribute()
    }

    fun setDateSeparatorFontSize(size: Float) {
        attribute.dateSeparatorFontSize = size
        setAttribute()
    }

    private fun setAttribute() {
        messageAdapter.setAttribute(attribute)
    }

    private val firebaseDummyURL: String
        get() = "chatroom"

    private val firebaseChatCollection: String
        get() = "chat"

    private val firebaseChatDoc: String
        get() = MoaiPayGlobal.User!!.key!!.id

    private val chatHashMapTemplate
        get() = hashMapOf<String, Any>(
            "content" to "",
            "createData" to "",
            "message" to "",
            "moaiId" to "",
            "senderAvatar" to "",
            "senderId" to "",
            "senderName" to "",
            "type" to ""
        )
    private val firebaseDB = FirebaseFirestore.getInstance()


    var dummyListener: ListenerRegistration? = null
    val list = ArrayList<DummyContent.DummyData>()

    fun getData() {
        val queryAccount = firebaseDB.collection("chatroom")

        dummyListener?.remove()
        dummyListener = queryAccount.addSnapshotListener { result, e ->
            if (e == null) {
                if (result == null) {
                    return@addSnapshotListener
                }
                if (result.documentChanges.count() < 1) {
                    return@addSnapshotListener
                }

                result.documentChanges.forEach { docChange ->
                    val group = docChange.document.toObject(DummyContent.DummyData::class.java)
                    group.key = docChange.document.reference
                    group.id = group.key!!.id
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> list.add(group)
                        DocumentChange.Type.REMOVED -> list.remove(group)
                        DocumentChange.Type.MODIFIED -> {
                            val mod = list.filter { it.key == group.key }
                            list.remove(mod.first())
                            list.add(group)
                        }
                    }
                }
                val sortedList = ArrayList(list.sortedByDescending { it.name })
                //mView.renderDummyList(sortedList)
            }
        }
    }

    fun submitData(text: String){

        val chatHashMap = HashMap<String, Any>(chatHashMapTemplate)
        chatHashMap["content"] = ""
        chatHashMap["createData"] = Timestamp.now()
        chatHashMap["message"] = text
        chatHashMap["moaiId"] = MoaiPayGlobal.User!!.key!!.id
        chatHashMap["senderAvatar"] = MoaiPayGlobal.User!!.avatar
        chatHashMap["senderId"] = MoaiPayGlobal.User!!.userId
        chatHashMap["senderName"] = MoaiPayGlobal.User!!.name
        chatHashMap["type"] = ""

        firebaseDB.collection(firebaseDummyURL).document(firebaseChatDoc).collection(firebaseChatCollection).document().set(chatHashMap)

//        mView.onTexMessageSent()

    }
}
