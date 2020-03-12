package com.ded.lyricsfinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.media.AudioManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)

        buttonListener()


    }

    fun buttonListener() {

        // Play button
        play_btn.setOnClickListener {
            val modeSelelector = Intent(this, GameModeSelection::class.java)
            startActivity(modeSelelector)
        }
        // Store button
        store_btn.setOnClickListener {
            val store = Intent(this, Store::class.java)
            startActivity(store)
        }
        // Settings button
        settings_btn.setOnClickListener {
            val settings = Intent(this, Settings::class.java)
            startActivity(settings)
        }
    }
}
