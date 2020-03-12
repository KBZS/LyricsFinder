package com.ded.lyricsfinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game_mode_selection.*

class GameModeSelection : AppCompatActivity() {

    private var currentLvl = 5
    private var currentXP = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode_selection)

        lvlUpdate()
        modesUpdate()

        buttonListener()

    }

    /*
    Updating the level counter
     */
    fun lvlUpdate() {

        val prefs = getSharedPreferences(getString(R.string.gameResources), Context.MODE_PRIVATE)
        currentXP = prefs.getInt(getString(R.string.xp), 0)
        currentLvl = prefs.getInt(getString(R.string.lvl), 4)



        //  Displaying current player level
        val level = findViewById<View>(R.id.level_counter) as TextView
        level.text = currentLvl.toString()

        // Displaying the current level progress (current XP)
        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar.progress = currentXP
    }

    /*
    'Greying out' game modes which are still locked (if level is too low)
     */
    fun modesUpdate() {

        // 80's mix
        val eighties = findViewById<View>(R.id.eight_btn) as Button
        val eightiesUnlock = findViewById<View>(R.id.eight_unlock) as TextView
        if(currentLvl >= 5) {
            eighties.isEnabled = true
            eightiesUnlock.visibility = View.INVISIBLE
        } else {
            eighties.isEnabled = false
        }
        // Running in the 90's
        val nineties = findViewById<View>(R.id.nine_btn) as Button
        val ninetiesUnlock = findViewById<View>(R.id.nine_unlock) as TextView
        if(currentLvl >= 10) {
            nineties.isEnabled = true
            ninetiesUnlock.visibility = View.INVISIBLE
        } else {
            nineties.isEnabled = false
        }
        // Songs from movies
        val movies = findViewById<View>(R.id.movies_btn) as Button
        val moviesUnlock = findViewById<View>(R.id.movies_unlock) as TextView
        if(currentLvl >= 15) {
            movies.isEnabled = true
            moviesUnlock.visibility = View.INVISIBLE
        } else {
            movies.isEnabled = false
        }
    }

    /**
    Listener for the mode selection buttons
     */
    fun buttonListener() {

        // Classic mode (0)
        classic_btn.setOnClickListener {
            val gameMap = Intent(this, GameMap::class.java)
            // Passing a value to detect which mode was selected
            gameMap.putExtra("selectedMode", "classic")
            startActivity(gameMap)
        }
        // Current mode (1)
        current_btn.setOnClickListener {
            val gameMap = Intent(this, GameMap::class.java)
            // Passing a value to detect which mode was selected
            gameMap.putExtra("selectedMode", "current")
            startActivity(gameMap)
        }
        // 80's mode (2)
        eight_btn.setOnClickListener {
            val gameMap = Intent(this, GameMap::class.java)
            // Passing a value to detect which mode was selected
            gameMap.putExtra("selectedMode", "eighties")
            startActivity(gameMap)
        }
        // 90's mode (3)
        nine_btn.setOnClickListener {
            val gameMap = Intent(this, GameMap::class.java)
            // Passing a value to detect which mode was selected
            gameMap.putExtra("selectedMode", "nineties")
            startActivity(gameMap)
        }
        // Movies mode (4)
        movies_btn.setOnClickListener {
            val gameMap = Intent(this, GameMap::class.java)
            // Passing a value to detect which mode was selected
            gameMap.putExtra("selectedMode", "movies")
            startActivity(gameMap)
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences("generalLyrics", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("isChanged", false)
        editor.putString("isSolved", null)
        editor.apply()

    }

    override fun onResume() {
        super.onResume()
        lvlUpdate()
        modesUpdate()
    }

}
