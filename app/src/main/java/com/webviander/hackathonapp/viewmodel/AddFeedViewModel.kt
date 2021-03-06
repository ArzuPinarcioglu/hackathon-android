package com.webviander.hackathonapp.viewmodel

import android.app.Activity
import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import com.webviander.hackathonapp.data.ApiFactory
import com.webviander.hackathonapp.model.AddPostModel
import com.webviander.hackathonapp.util.PreferenceUtil
import com.webviander.hackathonapp.view.FeedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by vivek-3102 on 28/09/17.
 */
class AddFeedViewModel(val context: Context) : Observable() {
    val backPressedTag = "BACK_PRESSED"
    val savePostTag = "SAVE_POST"

    var postBody: ObservableField<String> = ObservableField("")
    var postTitle: ObservableField<String> = ObservableField("")

    fun onBackClick(view: View) {
//        setChanged()
//        notifyObservers(backPressedTag)
        context.startActivity(FeedActivity.getIntent(context))
        (context as Activity).finish()

    }

    fun onSavePost(view: View) {
        Log.d("SavePost", postBody.get())
        setChanged()
        notifyObservers(savePostTag)
    }

    fun addPostApi(lat: String, lng: String) {
        Log.d("addPostApi", "$lat $lng")
        val userId = Prefs.getString(PreferenceUtil.USERID, null)
        userId?.let {
            ApiFactory().createFeedsService().addPost(userId, postTitle.get(), postBody.get(), lat, lng).enqueue(object : Callback<AddPostModel> {
                override fun onResponse(call: Call<AddPostModel>?, response: Response<AddPostModel>?) {
                    Log.d("onResponse", "called ${response?.body()} ${call?.request()?.url()}")
                    context.startActivity(FeedActivity.getIntent(context))
                    (context as Activity).finish()
                }

                override fun onFailure(call: Call<AddPostModel>?, t: Throwable?) {
                    Log.d("onFailure", "called ")
                    Toast.makeText(context,"Something went wrong! Please try later",Toast.LENGTH_SHORT).show()
                    t?.printStackTrace()
                    context.startActivity(FeedActivity.getIntent(context))
                    (context as Activity).finish()
                }
            })
        }
    }
}