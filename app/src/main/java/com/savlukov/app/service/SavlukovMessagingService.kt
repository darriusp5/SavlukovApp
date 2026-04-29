package com.savlukov.app.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SavlukovMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // TODO: Send token to server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "From: ${message.from}")
        
        // Check if message contains data payload
        if (message.data.isNotEmpty()) {
            Log.d("FCM", "Message data payload: ${message.data}")
        }

        // Check if message contains notification payload
        message.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
        }
    }
}
