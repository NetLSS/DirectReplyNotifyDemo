package com.lilcode.example.directreplynotifydemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lilcode.example.directreplynotifydemo.databinding.ActivityMainBinding
import androidx.core.content.ContextCompat
import android.app.RemoteInput

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var notificationManager: NotificationManager? = null
    private val channelID = "com.lilcode.example.directreplynotifydemo.news"

    private val notificationId = 101
    private val KEY_TEXT_REPLY = "key_text_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        createNotificationChannel(channelID, "DirectReply News", "Example News Channel")
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            NotificationChannel(id, name, importance).apply {
                this.description = description
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notificationManager?.createNotificationChannel(this)
            }
        }
    }

    fun sendNotification(view: View) {
        val replyLabel = "Enter your reply hehe"
        // key 와 label 로 RemoteInput 객체 생성
        val remoteInput = androidx.core.app.RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()

        // 팬딩 인텐트 생성하기
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val replyAction = NotificationCompat.Action.Builder(android.R.drawable.ic_dialog_info, "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput) // remote input 추가
            .build()

        val newMessageNotification = NotificationCompat.Builder(this, channelID)
            .setColor(ContextCompat.getColor(this, R.color.design_default_color_primary))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("My Notification")
            .setContentText("This is a test message")
            .addAction(replyAction)
            .build()

        notificationManager?.notify(notificationId, newMessageNotification)
    }
}