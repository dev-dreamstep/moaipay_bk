package com.dreamstep.moaipay.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.schinizer.rxunfurl.RxUnfurl
import com.schinizer.rxunfurl.model.PreviewData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.card_url_preview.view.*
import java.util.*

object ViewUtils {

    fun hideView(vararg views: View?){
        views.forEach {
            if(it != null)
                it.visibility = View.GONE }
    }

    fun showView(vararg views: View?){
        views.forEach {
            if(it != null)
                it.visibility = View.VISIBLE }
    }

    fun invisibleView(vararg views: View?){
        views.forEach {
            if(it != null)
                it.visibility = View.INVISIBLE }
    }

    fun renderImage(url:String?, img:ImageView, context: Context){
        Glide.with(context).load(url).into(img)
    }

    fun putText(textView:TextView, content:String?){
        textView.text = content
    }

    @SuppressLint("CheckResult")
    fun renderUrl(url:String, view:View){
        Log.i("KIZUNA-URL","PROCESAMEINTO DE URL = ${url}")
        val urlAux = if(url.contains("http://") || url.contains("https://") ){
            url
        }else{
            "http://$url"
        }
        try{
            RxUnfurl.Builder()
                .build()
                .generatePreview(urlAux.replace("wwww.",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Consumer { println("Error-rendering-url"); renderWrongUrl(view,url)})
                .subscribeWith(object : DisposableSingleObserver<PreviewData>() {
                    override fun onSuccess(previewData: PreviewData) {
                        println("DATOS DE URL ==== ${previewData.description}")
                        println("DATOS DE URL ==== ${previewData.title}")
                        println("DATOS DE URL ==== ${previewData.url}")
                        populateView(previewData, view)
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        }catch (ex:Exception){
            Log.e("MOAIPAY-URL","ERROR, PROCESANDO URL = $url, ERROR=${ex.localizedMessage}")
        }

    }

    private fun renderWrongUrl(view: View, url: String) {
        view.title?.text = "Error"
        view.textcontet?.text = ""
        view.host?.text = url
//        renderImageUrl("https://lh3.googleusercontent.com/.........", view)
    }

    fun populateView(data: PreviewData, view:View) {
        val uri = Uri.parse(data.url)
        if (!data.title.isEmpty()) view.title?.text = data.title
        if (!data.description.isEmpty()) view.textcontet?.text = data.description
        if (!data.url.isEmpty()) view.host?.text = uri.host?.toUpperCase(Locale.getDefault())

        if (data.images.size != 0 && view.context != null) {
            showView(view.cardimageView)
//                if(Parse.getApplicationContext() != null){
//                    try {
//                        renderImageUrl(data, view)
//                    }catch (e:Exception){
//                        Log.e("MOAIPAY-URL","ERROR, PROCESANDO URL = AQUI ERROR=${e.localizedMessage}")
//
//                    }
//                }

        }
    }
//    private fun renderImageUrl(data: PreviewData, urlpreviewrfan: View) {
//        var requestOptions = RequestOptions().transforms(FitCenter(), RoundedCorners(20))
//
//        Glide.with(Parse.getApplicationContext())
//            .applyDefaultRequestOptions(requestOptions)
//            .load((data.images[0].source))
//            .into(urlpreviewrfan.cardimageView)
//
//    }
}
