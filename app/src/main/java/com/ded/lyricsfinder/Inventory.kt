package com.ded.lyricsfinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_inventory.*

class Inventory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        // Assigning numbers of resources
        dataUpdate()

        buttonListener()
    }

    fun dataUpdate() {
        val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
        val xpBooster = findViewById<View>(R.id.use_xp_booster) as TextView
        xpBooster.text = "XP booster (x" + prefs.getInt("xpBoosters", 0).toString() + ")"

        val coinBooster = findViewById<View>(R.id.use_coin_booster) as TextView
        coinBooster.text = "Coin booster (x" + prefs.getInt("coinBoosters", 0).toString() + ")"

        val hints = findViewById<View>(R.id.use_hint) as TextView
        hints.text = "Hint (x" + prefs.getInt("hints", 0).toString() + ")"
    }

    fun buttonListener() {

        use_hint.setOnClickListener { v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("hints", prefs.getInt("hints", 0)-1)
            editor.apply()
            dataUpdate()
            val snackbar = Snackbar.make(v, "You used a hint for one song.", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        use_coin_booster.setOnClickListener { v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("coinBoosters", prefs.getInt("coinBoosters", 0)-1)
            editor.apply()
            dataUpdate()
            val snackbar = Snackbar.make(v, "You used a coin booster for 15 min.", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        use_xp_booster.setOnClickListener { v ->
            val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("xpBoosters", prefs.getInt("xpBoosters", 0)-1)
            editor.apply()
            dataUpdate()
            val snackbar = Snackbar.make(v, "You used an XP booster for 15 min.", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }
}
