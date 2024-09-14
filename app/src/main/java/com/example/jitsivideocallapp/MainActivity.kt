package com.example.jitsivideocallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jitsivideocallapp.databinding.ActivityMainBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom("@SampleRoom202")
            .build()

        binding.btnJoin.setOnClickListener {

//            JitsiMeetActivity.launch(this,options)

            if (binding.editText.text.toString().isNotEmpty()){
                Intent(this,VideoCallActivity::class.java).apply {
                    putExtra("VideoCallActivity",binding.editText.text.toString())
                    startActivity(this)
                }

            }
        }
    }
}