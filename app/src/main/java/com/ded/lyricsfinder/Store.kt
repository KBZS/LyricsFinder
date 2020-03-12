package com.ded.lyricsfinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_store.*

class Store : AppCompatActivity() {

    var currentCoins = 0
    var xpBoosterPrice = 300
    var coinBoosterPrice = 400
    var oneHintPrice = 150
    var packHintPrice = 350


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        coinUpdate()
        pricesUpdate()

        buttonListener()
    }

    fun coinUpdate() {

        //  Displaying current amount of coins player has
        val coins = findViewById<View>(R.id.coins_amount) as TextView

        val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
        currentCoins = prefs.getInt("coins", 200)
        Log.e("sdg", prefs.getInt("coins", 0).toString())
        coins.text = prefs.getInt("coins", 400).toString()

    }

    fun pricesUpdate() {

        //  Displaying current amount of coins player has
        val xpBooster = findViewById<View>(R.id.xp_booster_price) as TextView
        xpBooster.text = xpBoosterPrice.toString()

        //  Displaying current amount of coins player has
        val coinBooster = findViewById<View>(R.id.coin_booster_price) as TextView
        coinBooster.text = coinBoosterPrice.toString()

        //  Displaying current amount of coins player has
        val oneHint = findViewById<View>(R.id.one_hint_price) as TextView
        oneHint.text = oneHintPrice.toString()

        //  Displaying current amount of coins player has
        val packHint = findViewById<View>(R.id.pack_hint_price) as TextView
        packHint.text = packHintPrice.toString()

    }

    fun buttonListener() {
        // Buy an xp booster
        xp_btn.setOnClickListener { v ->

            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("xpBoosters", prefs.getInt("xpBoosters", 0)+1)
            editor.putInt("coins", prefs.getInt("coins", 0) - xpBoosterPrice)
            editor.apply()
            coinUpdate()
            val snackbar = Snackbar.make(v, "You bought an XP booster!", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        // Buy a coin booster
        coin_btn.setOnClickListener {v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("coinBoosters", prefs.getInt("coinBoosters", 0)+1)
            editor.putInt("coins", prefs.getInt("coins", 0) - coinBoosterPrice)
            editor.apply()
            coinUpdate()
            val snackbar = Snackbar.make(v, "You bought a coin booster!", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        // Buy one hint
        hint_btn.setOnClickListener { v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("hints", prefs.getInt("hints", 0)+1)
            editor.putInt("coins", prefs.getInt("coins", 0) - oneHintPrice)
            editor.apply()
            coinUpdate()
            val snackbar = Snackbar.make(v, "You bought a hint", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        // Buy a pack of three hints
        three_hints_btn.setOnClickListener { v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("hints", prefs.getInt("hints", 0)+3)
            editor.putInt("coins", prefs.getInt("coins", 0) - packHintPrice)
            editor.apply()
            coinUpdate()
            val snackbar = Snackbar.make(v, "You bought 3 hints!", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
}
