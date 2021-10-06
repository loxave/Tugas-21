package com.zen4r17.android.simplepushnotification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.zen4r17.android.simplepushnotification.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {


    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyFirebaseMessagingService.sharedPref =
            getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etToken = findViewById<EditText>(R.id.etToken)
        val etMessage = findViewById<EditText>(R.id.etMessage)


        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            MyFirebaseMessagingService.token = it
            binding.etToken.setText(it)
        }
            .addOnFailureListener { e ->
                Timber.d("FCM TOKEN FAILED")
                e.printStackTrace()
            }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        btnSend.setOnClickListener {
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            val recipientToken = etToken.text.toString()
            if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {

                PushNotification(
                    NotificationData(title, message),
                    recipientToken
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
}