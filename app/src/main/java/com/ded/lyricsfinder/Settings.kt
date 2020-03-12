package com.ded.lyricsfinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager
import kotlinx.android.synthetic.main.activity_settings.*
import android.app.Dialog
import kotlinx.android.synthetic.main.activity_wipe_data.*


class Settings : AppCompatActivity() {

    var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        muteSwitch()
        wipeBtn()
    }

    override fun onStop() {
        super.onStop()

        val prefs = getSharedPreferences("muteSwitch", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean("isChecked", mute_switch.isChecked)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("muteSwitch", Context.MODE_PRIVATE)
        isChecked = prefs.getBoolean("isChecked", false)
        mute_switch.isChecked = isChecked
    }

    fun muteSwitch() {

        mute_switch.setOnCheckedChangeListener {
                _, isChecked ->

            if (isChecked) {
                // Muting
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)

            } else {
                // Unmuting
                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
            }
        }
    }

    fun wipeBtn() {
        wipe_btn.setOnClickListener {
            val myDialog = Dialog(this)
            myDialog.setContentView(R.layout.activity_wipe_data)
            myDialog.setTitle("Guesser dialog box")
            myDialog.yes_btn.setOnClickListener {
                getSharedPreferences("muteSwitch", 0).edit().clear().apply()
                getSharedPreferences("gameResources", 0).edit().clear().apply()
                myDialog.dismiss()
            }
            myDialog.no_btn.setOnClickListener {
                myDialog.dismiss()
            }
            myDialog.show()
        }
    }

}
