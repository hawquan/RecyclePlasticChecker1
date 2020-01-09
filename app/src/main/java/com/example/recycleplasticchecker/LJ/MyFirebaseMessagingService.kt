package com.example.recycleplasticchecker.LJ

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

private const val TAG = "MyFirebase"

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val refreshedToken = FirebaseMessaging.getInstance()
        Log.d(TAG, "Refreshed token" +refreshedToken!!)


    }
}
