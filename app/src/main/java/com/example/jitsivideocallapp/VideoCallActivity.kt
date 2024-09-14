package com.example.jitsivideocallapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jitsivideocallapp.databinding.ActivityVideoCallBinding
import com.facebook.react.modules.core.PermissionListener
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import org.jitsi.meet.sdk.JitsiMeetView
import timber.log.Timber
import java.net.URL
import java.time.Instant

class VideoCallActivity : AppCompatActivity() , JitsiMeetActivityInterface {

    private val binding : ActivityVideoCallBinding by lazy { ActivityVideoCallBinding.inflate(layoutInflater) }

    private lateinit var Jmeet : JitsiMeetView

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val video = binding.videoCall

        val callInt = intent
        val callId = callInt.getStringExtra("VideoCallActivity")

        val userInfo = JitsiMeetUserInfo().apply {
            displayName = "HiCodeLooper"
            email = "coder@example.com"
            avatar = URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc9APxkj0xClmrU3PpMZglHQkx446nQPG6lA&s")
        }

        Jmeet = JitsiMeetView(this)
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom(callId)
            .setFeatureFlag("invite.enabled",false)
            .setUserInfo(userInfo)
            .setFeatureFlag("pip.enabled", true)
            .setFeatureFlag("welcomepage.enabled", false)
            .build()

        Log.d("JitsiMeet Code room", "Joining conference as: ${userInfo.displayName}>>rom <${ options.room}")

        Jmeet?.let {
            JitsiMeetActivity.launch(this,options)
            it.join(options)
            video.addView(Jmeet)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter())
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(this,requestCode,resultCode,data)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        JitsiMeetActivityDelegate.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        JitsiMeetActivityDelegate.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode,permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        Jmeet?.dispose()
        JitsiMeetActivityDelegate.onHostDestroy(this)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    // Example for handling different JitsiMeetSDK events
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }
}