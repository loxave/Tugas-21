package com.zen4r17.android.simplepushnotification

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIdService : FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        val refreshToken = FirebaseMessaging.getInstance().token

        Log.e("refresh token", refreshToken.toString())

        sendRegistrationToServer(p0)
    }

    private fun sendRegistrationToServer(token: String?) {

        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }
}