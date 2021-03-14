package com.dbtechprojects.autosmsmarketing.util


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.dbtechprojects.autosmsmarketing.R
import com.dbtechprojects.autosmsmarketing.db.DatabaseHandler
import com.dbtechprojects.autosmsmarketing.db.entities.PhoneNumber
import com.dbtechprojects.autosmsmarketing.ui.MainActivity
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject




class PhoneCallReceiver : BroadcastReceiver() {



    override fun onReceive(arg0: Context, arg1: Intent) {

        if (arg1.action == "android.intent.action.PHONE_STATE") {
            val state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                Log.d(TAG, "Inside Extra state off hook")
                val number = arg1.getStringExtra(TelephonyManager.EXTRA_PHONE_ACCOUNT_HANDLE)
                Log.e(TAG, "outgoing number : $number")
            } else if (state == TelephonyManager.EXTRA_STATE_RINGING) {

                Log.e(TAG, "Inside EXTRA_STATE_RINGING")
                val number = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                // DB Stuff

                val phonenumber = number?.let { PhoneNumber(0,it,0) }

                if (phonenumber != null) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val db = DatabaseHandler(arg0)
                        db.addPhoneNumber(phonenumber)
                    }

                }

                Log.e(TAG, "incoming number : $number")

                // set up notification

                fun createNotificationChannel() {
                    // Create the NotificationChannel, but only on API 26+ because
                    // the NotificationChannel class is new and not in the support library
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val name = "Notification Channel"
                        val descriptionText = "Notification Channel for AutoSMSMarketing"
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel(1.toString(), name, importance).apply {
                            description = descriptionText
                        }
                        // Register the channel with the system
                        val notificationManager: NotificationManager? = arg0.getSystemService<NotificationManager>()

                        notificationManager?.createNotificationChannel(channel)
                    }
                }

                //Create an explicit intent for an Activity in your app
                val intent = Intent(arg0, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0)

                var builder = NotificationCompat.Builder(arg0, "1")
                        .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                        .setContentTitle("New Phone Call Received")
                        .setContentText("PhoneNumber: $number")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true) // cancel notification after being clicked on

                with(NotificationManagerCompat.from(arg0)) {
                    // notificationId is a unique int for each notification that you must define
                    createNotificationChannel()
                    notify(1, builder.build())
                }
            } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                Log.d(TAG, "Inside EXTRA_STATE_IDLE")
            }
        }
    }

}

