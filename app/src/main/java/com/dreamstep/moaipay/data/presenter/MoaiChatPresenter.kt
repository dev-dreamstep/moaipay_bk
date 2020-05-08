package com.dreamstep.moaipay.data.presenter

import android.content.Context
import com.dreamstep.moaipay.data.model.ChatMessage
import com.dreamstep.moaipay.interfaces.callback.MoaiChatCallback
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MoaiChatPresenter {
    private val context: Context,
    private val mView: MoaiChatCallback,
    private val mChat: ChatMessage
) {

    // VARIABLES
    // ====================================================
    // => Components
    var postsList: MutableMap<String, ChatMessage> = mutableMapOf()
    var isInitialLoadDone = false

    // => Firebase
    private val firebaseDB = FirebaseFirestore.getInstance(FirebaseApp.getInstance("secondary"))
    private val firebaseStorageDB = FirebaseStorage.getInstance()
    var firebaseListenerRegistration: ListenerRegistration? = null

    // => Values
    private val fetchInitialPostsLength: Long = 20
    private val fetchMorePostsLength: Long = 10
    private var fetchTimes: Int = 0
    private val post = (context.applicationContext as KizunaParse).post
    private val fetchPostsLength: Long
        get () = fetchInitialPostsLength + (fetchMorePostsLength.times(fetchTimes))

    private val firebasePostsURL: String
        get() = "replys/${mChatRoom.objectId}_${post?.key!!.id}/threads"

//        get() = "conversations/${mChatRoom.objectId}/threads"

    private val firebaseRepliesURL: String
        get() = "replys/${mChatRoom.objectId}_${post!!.key!!.id}/threads"

    private val lastGeneralPostURL: String
        get() = post?.key!!.path

    private val lastGeneralPostCollectionURL: String
        get() {
            val urlComponentsList = ArrayList(lastGeneralPostURL.split("/"))
            return if (urlComponentsList.isEmpty())
                ""
            else
                "${urlComponentsList[0]}/${urlComponentsList[1]}/${urlComponentsList[2]}"
        }

    private val lastGeneralPostIdURL: String
        get() {
            val urlComponentsList = ArrayList(lastGeneralPostURL.split("/"))
            return if (urlComponentsList.isEmpty())
                ""
            else
                urlComponentsList.last()
        }

    // => Templates
    // =>                 "sport" to mAthlete.getString("Sport")!!,
    private val messageHashMapTemplate
        get() = hashMapOf<String, Any>(
            "name" to currentUser.getString("name")!!,
            "avatar" to currentUser.getParseFile("avatar")!!.url,
            "fromID" to currentUser.objectId,
            "timestamp" to (System.currentTimeMillis() / 1000L).toInt(),
            "isRead" to false,
            "isPrivate" to false,
            "subscriber" to ( listOf(AppConstants.USER_CHAT_TYPE_SUPPORTER, AppConstants.USER_CHAT_TYPE_OWNER).contains(mUserChatType) ),
            "isAthlete" to (currentUser.getString("type") == AppConstants.USER_TYPE_ATHLETE),
            "ownerPosting" to (mUserChatType ==  AppConstants.USER_CHAT_TYPE_OWNER),
            "chatId" to mChatRoom.objectId,
            "sport" to mAthlete.getString("Sport")!!,
            "locale" to CommonUtils.deviceLanguage,
            "updatedAt" to "",
            "atheleteCommentsCount" to 0,
            "athletesComentCount" to 0,
            "fanCommentsCount" to 0,
            "giftCommentsCount" to 0,
            "athletesComentUrl" to ArrayList<String>(),
            "fanCommentsUrl" to ArrayList<String>(),
            "giftCommentsUrl" to ArrayList<String>(),
            "giftUsersId" to ArrayList<String>(),
            "giftImgUrls" to ArrayList<String>()
        )

    // FUNCTIONS
    // ====================================================
    init{
        firebaseStorageDB.maxUploadRetryTimeMillis = 30000
        fetchUserChatType()
    }

    fun fetchUserChatType(){

        mView.showLoading()

        when {

            (mAthlete.objectId == currentUserId) -> setUserChatType(AppConstants.USER_CHAT_TYPE_OWNER)

            (!mAthlete.getBoolean("supporter_club")) -> {
                ParseQuery.getQuery(Follows::class.java)
                    .whereEqualTo("userBy", currentUser)
                    .whereEqualTo("athlete", mAthlete)
                    .setLimit(1)
                    .findInBackground { isFollowerEntries, eFollow ->

                        if (eFollow == null) {

                            if (isFollowerEntries.size > 0)
                                setUserChatType(AppConstants.USER_CHAT_TYPE_FOLLOWER)
                            else
                                setUserChatType(AppConstants.USER_CHAT_TYPE_GUEST)

                        } else {
                            Log.d("DEBUG", "Fail on fetchUserChatType...")
                            eFollow.message?.let { mView.onError(it) }
                        }
                    }
            }

            else -> {
                ParseQuery.getQuery(Supports::class.java)
                    .whereEqualTo("userBy", currentUser)
                    .whereEqualTo("athlete", mAthlete)
                    .setLimit(1)
                    .findInBackground { isSupporterEntries, eSupport ->

                        if (eSupport == null) {

                            if (isSupporterEntries.size > 0)
                                setUserChatType(AppConstants.USER_CHAT_TYPE_SUPPORTER)
                            else {

                                ParseQuery.getQuery(Follows::class.java)
                                    .whereEqualTo("userBy", currentUser)
                                    .whereEqualTo("athlete", mAthlete)
                                    .setLimit(1)
                                    .findInBackground { isFollowerEntries, eFollow ->

                                        if (eFollow == null) {

                                            if (isFollowerEntries.size > 0)
                                                setUserChatType(AppConstants.USER_CHAT_TYPE_FOLLOWER)
                                            else
                                                setUserChatType(AppConstants.USER_CHAT_TYPE_GUEST)

                                        } else {
                                            Log.d("DEBUG", "Fail on fetchUserChatType...")
                                            eFollow.message?.let { mView.onError(it) }
                                        }
                                    }

                            }

                        } else {
                            Log.d("DEBUG", "Fail on fetchUserChatType...")
                            eSupport.message?.let { mView.onError(it) }
                        }
                    }
            }
        }
    }
    fun fetchFirebasePostsList() {

        // チャットオーナーと投稿ユーザーが違う場合
        if (this.mAthlete.objectId != this.post?.fromID) {
            postsList[this.post!!.key.toString()] = this.post
            mView.renderPostsList(ArrayList(postsList.values), false)
            ++fetchTimes
            mView.hideSwipeLoading()
            return
        }

        println("FIREBASE URL ==== $firebasePostsURL")
        val ref =
            firebaseDB
                .collection(firebasePostsURL)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(fetchPostsLength)

        firebaseListenerRegistration?.remove()
        firebaseListenerRegistration = ref.addSnapshotListener { queryResults, e ->
            var onlyMod = false
            if (e == null) {

                for(docChange in queryResults!!.documentChanges){

                    val post = docChange.document.toObject(ChatMessage::class.java)
                    post.key = docChange.document.reference
                    post.chatId = mChatRoom.objectId

                    when (docChange.type) {

                        DocumentChange.Type.ADDED ->
                            postsList[docChange.document.reference.toString()] = post

                        DocumentChange.Type.MODIFIED ->
                            onlyMod = true

                        DocumentChange.Type.REMOVED ->
                            postsList.remove(docChange.document.reference.toString())

                    }

                }

                // 「ファンからのコメント」ファン起点メッセージグループ（2.X系）
                if (this.post?.fanMessages!!.count() > 0) {
                    val msgs = this.post.fanMessages
                    msgs.forEach { msg ->
                        postsList[msg.key.toString()] = msg
                    }
                }

                val sortedPostList = ArrayList(postsList.values.sortedBy{ it.timestamp })
                var isMyPost = false
                if ( sortedPostList.isNotEmpty() ) {
                    lastGeneralPost = sortedPostList.last()
                    if (fetchTimes > 0 && lastGeneralPost?.fromID == currentUserId)
                        isMyPost = true
                }
                if (!onlyMod) {
                    mView.renderPostsList(sortedPostList, isMyPost)
                    ++fetchTimes
                    mView.hideSwipeLoading()
                }
            }
            else {
                Log.d("DEBUG", "Fail on fetchFirebasePostsList...")
                e.message?.let { mView.onError(it) }
            }
        }
    }
    fun fetchStickersListAndCategories(){

        val query = ParseQuery<ParseObject>("StickerCategory")
        query.whereEqualTo("enable", true)
//        query.cachePolicy = ParseQuery.CachePolicy.CACHE_THEN_NETWORK
        query.orderByAscending("order")
        query.findInBackground{ stickerCategories,e ->

            if (e == null){

                Log.d("DEBUG", "Success on: fetchStickerCategories. stickerCategories size: ${stickerCategories.size}")

                val stickersQuery = ParseQuery<ParseObject>("Sticker")
                stickersQuery.whereEqualTo("develop", true)
//                stickersQuery.cachePolicy = ParseQuery.CachePolicy.CACHE_THEN_NETWORK
                stickersQuery.orderByAscending("order")
                stickersQuery.findInBackground{ stickers,eStickers ->

                    if (eStickers == null){

                        Log.d("DEBUG", "Success on: fetchStickerCategories. stickers size: ${stickers.size}")

                        mView.renderStickersListAndCategories( ArrayList(stickers), ArrayList(stickerCategories) )

                    }
                    else {
                        Log.d("DEBUG", "Fail on fetchStickers...")
                        eStickers.message?.let { mView.onError(it) }
                    }
                }

            }
            else {
                Log.d("DEBUG", "Fail on fetchStickerCategories...")
                e.message?.let { mView.onError(it) }
            }
        }
    }

    fun sharePost(post: ChatMessage, chatRoom: ChatRoom, activity:ChatRoomThreadActivity) {

        val ios = DynamicLink.IosParameters.Builder("oceans.inc.company.kizuna")
            .setAppStoreId(AppConstants.APP_STORE_ID)
        if( post.type == context.getString(R.string.message_type_photo) ) {
            val image_noencoded:String = if (!post.imgsUrl.isEmpty())
                post.imgsUrl[0]
            else if(post.video != "")
                post.thumbVideoImg
            else if (post.gift == true)
                post.giftUrl!!
            else
                post.avatar!!

            val name_noencoded:String = post.name ?: ""

            val social = DynamicLink.SocialMetaTagParameters.Builder()
                .setImageUrl(Uri.parse(image_noencoded))
                .setTitle("${name_noencoded} ")
                .setDescription("OPENボタンから、KIZUNAにアクセス！アスリートとコミュニケーションしよう！").build()


            val link = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://kizuna-athletes.jp/chat/${chatRoom.objectId}"))
                .setDynamicLinkDomain("kizunaathletes.page.link")
                .setAndroidParameters( DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters( ios.build())

                .setSocialMetaTagParameters(social)
                .buildShortDynamicLink().addOnCompleteListener{

                    val shortLink = it.result!!.shortLink
                    val flowchartLink = it.result!!.previewLink
                    ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setText("KIZUNAで、アスリートとコミュニケーションしよう！ ${shortLink.toString()}")
                        .startChooser()

                }
        }else{

            val avatar_noencoded:String = post.avatar ?: ""
            val content_noencoded:String = post.content ?: ""
            val name_noencoded:String = post.name ?: ""

            val content_encoded = URLEncoder.encode(content_noencoded, "utf-8")
            val avatar_encoded = URLEncoder.encode(avatar_noencoded, "utf-8")

            val name_encoded = URLEncoder.encode(name_noencoded, "utf-8")

            val string_url = "https://us-central1-kizunanative.cloudfunctions.net/shareText?sport=Fan&text=${content_encoded}&user_name=${name_encoded}&user_image_url=${avatar_encoded}"

            val social = DynamicLink.SocialMetaTagParameters.Builder()
                .setImageUrl(Uri.parse(string_url))
                .setTitle("${name_noencoded} ")
                .setDescription("OPENボタンから、KIZUNAにアクセス！アスリートとコミュニケーションしよう！").build()


            val link = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://kizuna-athletes.jp/chat/${chatRoom.objectId}"))
                .setDynamicLinkDomain("kizunaathletes.page.link")
                .setAndroidParameters( DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters( ios.build())

                .setSocialMetaTagParameters(social)
                .buildShortDynamicLink().addOnCompleteListener{

                    val shortLink = it.result!!.shortLink
                    ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setText("KIZUNAで、アスリートとコミュニケーションしよう！ ${shortLink.toString()}")
                        .startChooser()
                }
        }

    }
    fun mustBeFollowerDialog(post: ChatMessage, context:Context, mAthlete:ParseObject,
                             mChatRoom: ChatRoom, hastPost:Boolean, withGift:Boolean) {

        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.resources.getText(R.string.replayComment))
        builder.setPositiveButton(context.resources.getText(R.string.toFollow)){dialog, which ->
            val follow = Follows()
            follow.put("athlete", mAthlete)
            follow.put("userBy", ParseUser.getCurrentUser())
            follow.saveInBackground {
                if(it == null){
                    val i = newThreadIntent(context, mChatRoom, mAthlete as ParseUser, hastPost, withGift)
                    (context.applicationContext as KizunaParse).post = post
                    context.startActivity(i)
                }else{
                    dialog.dismiss()
                }
            }
        }
        builder.setNegativeButton(context.resources.getText(R.string.cancel)){_,_ ->
            return@setNegativeButton
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()


    }

    // SUBMIT MESSAGES
    // =================================================================
    // => TextMessage
    fun submitTextMessage(isPostMessage: Boolean, textMessage: String){

        val messageHashMap = HashMap<String, Any>(messageHashMapTemplate)
        messageHashMap["type"] = context.getString(R.string.message_type_text)
        messageHashMap["content"] = textMessage
        println("FIREBASE URL ==== $firebasePostsURL")
        firebaseDB
            .collection(
                if (isPostMessage)
                    firebasePostsURL
                else
                    firebaseRepliesURL
            )
            .document(CommonUtils.randomString)
            .set(messageHashMap)

        onMessageCreatedDecideNext(isPostMessage, textMessage)

        mView.onTexMessageSent()

    }
    // =================================================================
    // => ImageMessage
    fun submitImageMessage(isPostMessage: Boolean, images: java.util.ArrayList<Image>, textMessage: String){

        if(isPostMessage)
            saveItemsToParseMedia( ArrayList(images) )

        insertImagesToFirebaseAndCreateMessage( isPostMessage, images, textMessage )

    }
    private fun insertImagesToFirebaseAndCreateMessage(isPostMessage: Boolean, images: java.util.ArrayList<Image>, textMessage: String) {

        val firebaseImagesURLs = ArrayList<String>()

        images.map { image ->

            val imageRealPath = CommonUtils.getRealPathFromURI( context, Uri.parse(image.path))
//            val imageRealPathUri = Uri.parse( imageRealPath )
            val imageUri = Uri.fromFile(File(image.path))
            val tempBitmap = CommonUtils.getBitmapFromUri(context, imageUri)
            val matrix = Matrix()
//            val realUri = CommonUtils.getRealPathFromURI( context, imageRealPathUri )

//            val ei = ExifInterface( Uri.parse(imageRealPath).path )
            val ei = ExifInterface( imageRealPath )

            when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)) {

//                ExifInterface.ORIENTATION_NORMAL ->  tempBitmap
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
//                else -> tempBitmap
            }

            try {

                val newBitmap =
                    Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.width, tempBitmap.height, matrix, true)
                newBitmap.compress(Bitmap.CompressFormat.JPEG,100, context.contentResolver.openOutputStream(imageUri))

                //val  newPath = MediaStore.Images.Media.insertImage(context.contentResolver, imageUri.path, "chat", null)
                val fileRef = firebaseStorageDB.reference.child("messagePics/${UUID.randomUUID()}.jpg")

                fileRef.putFile(imageUri).addOnSuccessListener { _ ->

                    fileRef.downloadUrl.addOnSuccessListener{

                        firebaseImagesURLs.add(it.toString())
                        if(firebaseImagesURLs.size == images.size){

                            CommonUtils.cleanGeneratedFiles(context)
                            createImageMessage(isPostMessage, firebaseImagesURLs, textMessage)

                        }

                    }
                }.addOnFailureListener { exception: Exception ->
                    Log.d("DEBUG", "Fail on submitImageMessage...")
                    exception.message?.let {
                        mView.onError(it)
                        mView.onImageUploadError()
                    }
                }

            } catch (e: OutOfMemoryError) {
                Log.d("DEBUG", "Fail on submitImageMessage...")
                e.message?.let {
                    mView.onError(it)
                    mView.onImageUploadError()
                }
                e.printStackTrace()
            }
        }

    }
    private fun createImageMessage(isPostMessage: Boolean, firebaseImagesURLs: ArrayList<String>, textMessage: String){

        val messageHashMap = HashMap<String, Any>(messageHashMapTemplate)
        messageHashMap["type"] = context.getString(R.string.message_type_photo)
        messageHashMap["content"] = textMessage
        messageHashMap["imgsUrl"] = firebaseImagesURLs
        println("FIREBASE URL ==== $firebasePostsURL")

        firebaseDB
            .collection(
                if (isPostMessage)
                    firebasePostsURL
                else
                    firebaseRepliesURL
            )
            .document(CommonUtils.randomString)
            .set(messageHashMap)

        onMessageCreatedDecideNext(
            isPostMessage,
            if (textMessage.isNotBlank())
                textMessage
            else
                context.getString(R.string.new_inserted_image_message)
        )

        mView.onImageMessageSent()
    }
    // =================================================================
    // => VideoMessage
    fun submitVideoMessage(isPostMessage: Boolean, videoUri: Uri, textMessage: String){

//        if(isPostMessage)
//            saveItemsToParseMedia(arrayListOf(videoUri))

        compressVideoAndCreateMessage( isPostMessage, videoUri, textMessage )

    }
    private fun compressVideoAndCreateMessage(isPostMessage: Boolean, videoUri: Uri, textMessage: String) {

        val originalVideoPath = videoUri.path
        val firstCompressVideoPath = context.filesDir.absolutePath + File.separator + "file.mp4"
        val secondCompressVideoPath = context.filesDir.absolutePath + File.separator + "file1.mp4"
        val tempCompressedVideoFile = File(firstCompressVideoPath)
        val compressedVideoFile =  File(secondCompressVideoPath)

        val fileSizeUnit = 1020

        VideoCompress.compressVideoLow(originalVideoPath, firstCompressVideoPath, object : VideoCompress.CompressListener {

            override fun onStart() { Log.d("DEBUG", "Video Compression #1: << onStart >>") }
            override fun onFail() {
                mView.onError("Fail on compressVideoAndCreateMessage... Video Compression #1")
                mView.onVideoNotCompatible()
                mView.onVideoUploadError()
                Log.e("DEBUG", "Video Compression #1: << onFail >> - Fail on compressVideoAndCreateMessage...")
            }
            override fun onProgress(percent: Float) { Log.d("DEBUG", "Video Compression #1: << onProgress >> $percent%")}
            override fun onSuccess() {

                Log.d("DEBUG", "Video Compression #1: << onSuccess >> - Video size: ${File(firstCompressVideoPath).length()/fileSizeUnit}")
                VideoCompress.compressVideoLow(firstCompressVideoPath, secondCompressVideoPath, object : VideoCompress.CompressListener{

                    override fun onStart() { Log.d("DEBUG", "Video Compression #2: << onStart >>") }
                    override fun onFail() {
                        createVideoMessage(isPostMessage, firstCompressVideoPath, compressedVideoFile, textMessage)
                        mView.onError("Fail on compressVideoAndCreateMessage... Video Compression #2")
                        mView.onVideoNotCompatible()
                        Log.e("DEBUG", "Video Compression #2: << onFail >> - Fail on compressVideoAndCreateMessage...")
                    }
                    override fun onProgress(percent: Float) { Log.d("DEBUG", "Video Compression #2: << onProgress >> $percent%")}
                    override fun onSuccess() {

                        Log.d("DEBUG", "Video Compression #2: << onSuccess >> - Video size: ${File(secondCompressVideoPath).length()/fileSizeUnit}")
                        createVideoMessage(isPostMessage, secondCompressVideoPath, compressedVideoFile, textMessage)

                    }
                })
            }
        })
    }

    fun createVideoMessage(isPostMessage: Boolean, compressedVideoPath: String, compressedVideoFile: File, textMessage: String) {



        try {
            val videoRealPath = CommonUtils.getRealPathFromURI(context, Uri.fromFile(File(compressedVideoPath)))
            val videoRealPathUri = Uri.parse(videoRealPath)
            val retriever =  MediaMetadataRetriever()
            try {
                retriever.setDataSource( context, Uri.parse(compressedVideoPath) )
            } catch (ex: Exception) {
                retriever.setDataSource(context, videoRealPathUri)
            }
            val outputStream = ByteArrayOutputStream()
            val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            bitmap.recycle()
            val videoThumbnailImageFile = ParseFile("image.png", byteArray)

            val media = ParseObject.create("Media")
            if (isPostMessage)
                media.put("athlete", mAthlete)
            media.put("image", videoThumbnailImageFile)
            media.put("video", ParseFile(compressedVideoFile))
            media.put("notify", false)
            media.saveInBackground{ e ->

                if (e == null){

                    val messageHashMap = HashMap<String, Any>(messageHashMapTemplate)
                    messageHashMap["type"] = context.getString(R.string.message_type_photo)
                    messageHashMap["content"] = textMessage
                    messageHashMap["thumbVideoImg"] = media.getParseFile("image")!!.url
                    messageHashMap["video"] = media.getParseFile("video")!!.url
                    println("FIREBASE URL ==== $firebasePostsURL")

                    firebaseDB
                        .collection(
                            if (isPostMessage)
                                firebasePostsURL
                            else
                                firebaseRepliesURL
                        )
                        .document(CommonUtils.randomString)
                        .set(messageHashMap)

                    onMessageCreatedDecideNext(
                        isPostMessage,
                        if (textMessage.isNotBlank())
                            textMessage
                        else
                            context.getString(R.string.new_inserted_video_message)
                    )

                    mView.onVideoMessageSent()

                }
                else {
                    Log.d("DEBUG", "Fail on createVideoMessage...")
                    mView.onError("Fail on createVideoMessage...")
                    mView.onVideoUploadError()
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            Log.d("DEBUG", "Fail on createVideoMessage...")
            mView.onError("Fail on createVideoMessage...")
            mView.onVideoNotCompatible()
            mView.onVideoUploadError()
        }
    }
    // =================================================================
    // => StickerMessage
    fun submitStickerMessage(isPostMessage: Boolean, sticker: ParseObject) {

        val fileRef = firebaseStorageDB.reference.child("messagePics/" + UUID.randomUUID().toString() + ".jpg")

        val stickerPFile = sticker.getParseFile("image")
        val stickerImageFile = stickerPFile?.file
        val contentUri = Uri.fromFile(stickerImageFile)

        fileRef.putFile(contentUri).addOnSuccessListener { _ ->

            fileRef.downloadUrl.addOnSuccessListener {

                firebaseDB
                    .collection(
                        if (isPostMessage)
                            firebasePostsURL
                        else
                            firebaseRepliesURL
                    )
                    .document(CommonUtils.randomString)
                    .update("giftUrl", it.toString())
            }

        }.addOnFailureListener { exception ->
            Log.d("DEBUG", "Fail on submitStickerMessage...")
            exception.message?.let { mView.onError(it) }
        }

        val messageHashMap = HashMap<String, Any>(messageHashMapTemplate)
        messageHashMap["type"] = context.getString(R.string.message_type_photo)
        messageHashMap["content"] = ""
        messageHashMap["gift"] = true
        messageHashMap["giftUrl"] = stickerPFile?.url!!
        messageHashMap.remove("locale")
        println("FIREBASE URL ==== $firebasePostsURL")

        firebaseDB
            .collection(
                if (isPostMessage)
                    firebasePostsURL
                else
                    firebaseRepliesURL
            )
            .document(CommonUtils.randomString)
            .set(messageHashMap)

        onMessageCreatedDecideNext(
            isPostMessage,
            context.getString(R.string.new_inserted_sticker_message)
        )

        mView.onStickerMessageSent()

    }
    // =================================================================
    // => GiftMessage
    fun submitGiftMessage(isPostMessage: Boolean, gift: ParseObject, textMessage: String, price: String, purchase: Purchase) {

        val fileRef = firebaseStorageDB.reference.child("messagePics/" + UUID.randomUUID().toString() + ".jpg")

        val giftImageFile = gift.getParseFile("image")?.file
        val contentUri = Uri.fromFile(giftImageFile)

        fileRef.putFile(contentUri).addOnSuccessListener { _ ->

            fileRef.downloadUrl.addOnSuccessListener {

                // Save to Parse
                val stickerBuy =  ParseObject.create("StickerBuy")
                stickerBuy.put("user", currentUser)
                stickerBuy.put("athlete", mAthlete)
                stickerBuy.put("date", Date(purchase.purchaseTime))
                stickerBuy.put("googleplay", purchase.sku)
                stickerBuy.put("iden", purchase.purchaseToken)
                stickerBuy.put("orderid", purchase.orderId)
                stickerBuy.put("price", price)
                stickerBuy.saveInBackground()

                // Save to Firebase
                val giftData = HashMap<String, Any>()
                giftData["playstore"] = purchase.sku
                giftData["iden"] = purchase.purchaseToken
                giftData["price"] = price

                val messageHashMap = HashMap<String, Any>(messageHashMapTemplate)
                messageHashMap["type"] = context.getString(R.string.message_type_photo)
                messageHashMap["gift"] = true
                messageHashMap["content"] = textMessage
                messageHashMap["giftUrl"] = it.toString()
                messageHashMap["giftData"] = giftData
                println("FIREBASE URL ==== $firebasePostsURL")

                firebaseDB
                    .collection(
                        if (isPostMessage)
                            firebasePostsURL
                        else
                            firebaseRepliesURL
                    )
                    .document(CommonUtils.randomString)
                    .set(messageHashMap)

                onMessageCreatedDecideNext(
                    isPostMessage,
                    context.getString(R.string.new_inserted_gift_message),
                    it.toString()
                )

                mView.onGiftMessageSent()
            }

        }.addOnFailureListener { exception ->
            Log.d("DEBUG", "Fail on submitGiftMessage...")
            exception.message?.let { mView.onError(it) }
        }
    }
    // =================================================================
    // => Helpers
    fun hideMessageWithKeyId(hiddenMessageKeyId: String){
        currentUser.addUnique("hidden", hiddenMessageKeyId)
        currentUser.saveInBackground {e ->
            if (e == null) {

                mView.onMessageHidden(hiddenMessageKeyId)

            } else{
                Log.d("DEBUG", "Fail on hideMessageWithKeyId...")
                e.message?.let{ mView.onError(it) }
            }
        }
    }
    fun blockUser(userId: String){
        currentUser.addUnique("blocked", userId)
        currentUser.saveInBackground {e ->
            if (e == null) {

                mView.onUserBlocked(userId)

            } else{
                Log.d("DEBUG", "Fail on blockUser...")
                e.message?.let{ mView.onError(it) }
            }
        }
    }
    fun reportUser(userId: String){
        val reports = ParseObject.create("Reports")
        reports.put("by", currentUser)
        reports.put("user", ParseUser.createWithoutData("_User", userId))
        reports.saveEventually()
    }
    private fun setUserChatType(userChatType: String){
        mUserChatType = userChatType
        mView.renderUserChatType(userChatType)
        if (!isInitialLoadDone) {
            fetchFirebasePostsList()
            isInitialLoadDone = true
        }
    }
    private fun onMessageCreatedDecideNext(isPostMessage: Boolean, lastMessageBody: String, giftMessageURL: String = "") {
        if (isPostMessage){
            updateChatRoomLastMessage(lastMessageBody)
        }
        else {
            incrementLastGeneralPostCounters(giftMessageURL)
        }
    }
    private fun updateChatRoomLastMessage(messageBody: String) {
        mChatRoom.put("lastMessage", currentUser.getString("name") + " : " + messageBody)
        mChatRoom.put("lastDate", Date())
        mChatRoom.saveInBackground { error ->
            if (error == null) {
                mView.refreshAthleteCommentCount()
            }
        }
    }
    private fun incrementLastGeneralPostCounters(giftMessageURL: String = "") {

        val lastGeneralPostRef =
            firebaseDB
                .collection(lastGeneralPostCollectionURL)
                .document(lastGeneralPostIdURL)

        lastGeneralPostRef.get().addOnCompleteListener {

            if (it.result != null) {

                val chatMessage = it.result!!.toObject(ChatMessage::class.java)!!

//              THIS CHANGE ACTIONED SERVER COUNTERS TRIGGER
                val updated = System.currentTimeMillis().toString()
                lastGeneralPostRef.update( "updatedAt", updated )

                if(messageHashMapTemplate["fromID"] != null && messageHashMapTemplate["fromID"] == mAthlete.objectId){
                    lastGeneralPostRef.update("atheleteCommentsCount", ++chatMessage.atheleteCommentsCount )
                }

                if(currentUser.get("type") == AppConstants.USER_TYPE_ATHLETE){
                    //EL USUARIO ES ATLETA
                    if(giftMessageURL.isNotEmpty()) {
                        lastGeneralPostRef.update( "giftCommentsCount", (1 + chatMessage.giftCommentsCount) )
                        lastGeneralPostRef.update( "giftUsersId", arrayListOf( *chatMessage.giftUsersId.toTypedArray(), currentUserId) )
                        lastGeneralPostRef.update( "giftCommentsUrl", arrayListOf( *chatMessage.giftCommentsUrl.toTypedArray(), currentUser.getParseFile("avatar")!!.url) )
                        lastGeneralPostRef.update( "giftImgUrls", arrayListOf( *chatMessage.giftImgUrls.toTypedArray(), giftMessageURL) )
                    }else{
                        lastGeneralPostRef.update( "athletesComentCount", (1 + chatMessage.athletesComentCount) )
                        lastGeneralPostRef.update( "athletesComentUrl", arrayListOf( *chatMessage.athletesComentUrl.toTypedArray() , currentUser.getParseFile("avatar")!!.url ) )
                    }
                }else{
                    //EL USUARIO ES FAN
                    if(giftMessageURL.isNotEmpty()) {
                        lastGeneralPostRef.update( "giftCommentsCount", (1 + chatMessage.giftCommentsCount) )
                        lastGeneralPostRef.update( "giftUsersId", arrayListOf( *chatMessage.giftUsersId.toTypedArray(), currentUserId) )
                        lastGeneralPostRef.update( "giftCommentsUrl", arrayListOf( *chatMessage.giftCommentsUrl.toTypedArray(), currentUser.getParseFile("avatar")!!.url) )
                        lastGeneralPostRef.update( "giftImgUrls", arrayListOf( *chatMessage.giftImgUrls.toTypedArray(), giftMessageURL) )
                    }else{
                        lastGeneralPostRef.update( "fanCommentsCount", (1 + chatMessage.fanCommentsCount) )
                        lastGeneralPostRef.update( "fanCommentsUrl", arrayListOf( *chatMessage.fanCommentsUrl.toTypedArray() , currentUser.getParseFile("avatar")!!.url ) )
                    }
                }
            }
        }
    }
    private fun saveItemsToParseMedia(items: ArrayList<Any>) {

        items.map{ item ->

            val imageFile =
                if(item is Image)
                    ImageFile(Uri.parse(item.path), UUID.randomUUID().toString())
                else
                    ImageFile((item as Uri), UUID.randomUUID().toString())

            val imageFileStream =
                if(item is Image)
                    imageFile.copyFileStream( context, Uri.parse(item.path) )
                else
                    imageFile.copyFileStream(context, (item as Uri))

            val imageCopyFile = File( imageFileStream )
            val imageCopyUri = Uri.fromFile(imageCopyFile)
            val imageCopyCloneFile = File(imageCopyUri.path)

            val media = ParseObject.create("Media")
            media.put("athlete", mAthlete)
            media.put("notify", false)
            media.put("image", ParseFile(imageCopyCloneFile))
            media.saveInBackground()

        }
    }
}