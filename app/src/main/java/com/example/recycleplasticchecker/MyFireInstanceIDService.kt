package com.example.recycleplasticchecker

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
private const val TAG = "MyFirebase"


class MyFireInstanceIDService : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        val refreshToken = FirebaseInstanceId.getInstance().getToken()
        Log.d(TAG, "Refereshed token" + refreshToken!!)
    }
}