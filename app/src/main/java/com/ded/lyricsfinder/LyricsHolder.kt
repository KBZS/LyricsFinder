package com.ded.lyricsfinder

import kotlin.collections.ArrayList
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ded.lyricsfinder.MyAdapter
import com.ded.lyricsfinder.MyModel
import kotlinx.android.synthetic.main.activity_game_map.*
import kotlinx.android.synthetic.main.activity_lyrics_holder.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView.OnItemClickListener




class LyricsHolder : AppCompatActivity() {

    private var collectedLyrics = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics_holder)

        val lyricsArrayList = intent.getStringArrayListExtra("lyrics")

        collectedLyrics = if((this.application as DataFinder).isHolderEmpty()) {
            lyricsArrayList
        } else (this.application as DataFinder).getUpdatedHolder()


        val prefs = getSharedPreferences("generalLyrics", Context.MODE_PRIVATE)
        var updateVar: String? = prefs.getString("isSolved", "")
        Log.e("PREFFS LYRICSHOLDER:", prefs.getString("isSolved", ""))
        collectedLyrics.remove(updateVar)

        (this.application as DataFinder).setNewHolder(collectedLyrics)

//        (this.application as DataFinder).setNewHolder(collectedLyrics)
//        Log.e("check DATA", (this.application as DataFinder).getUpdatedHolder()!!.get(0))

//        val prefs = getSharedPreferences("collected", Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putStringSet("collected", collectedLyrics.toMutableSet())
//        editor.apply()

        var imageModelArrayList: ArrayList<MyModel> = populateList()

        val recyclerView = findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = MyAdapter(imageModelArrayList)
        recyclerView.adapter = mAdapter

//        if (intent.getBooleanExtra("hint", false)) {
//
//        }


        val timer = object: CountDownTimer(500000, 300) {
            override fun onTick(millisUntilFinished: Long) {

                if(prefs.getBoolean("isChanged", false)) { // if true

                    val editor = prefs.edit()
                    editor.putBoolean("isChanged", false)
                    editor.apply()

                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            override fun onFinish() {}
        }
        timer.start()

//        fake_btn.setOnClickListener {
//            val myDialog = Dialog(this)
//            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            myDialog.setContentView(R.layout.activity_song_guesser)
//            myDialog.setTitle("Guesser dialog box")
//
//            myDialog.show()
//        }
    }

    private fun populateList(): ArrayList<MyModel> {
        val list = ArrayList<MyModel>()

        val myImageList = intArrayOf(R.drawable.greensmallnoteicon, R.drawable.yellowsmallnoteicon, R.drawable.redsmallnoteicon)

        collectedLyrics.sort()
        if(intent.getStringExtra("removedLyrics") != null) {
            Log.e("Removed", intent.getStringExtra("removedLyrics"))
        }


        for (i in 0 until collectedLyrics.size) {
            val model = MyModel()

            model.setLyrics(collectedLyrics[i].substringAfter(';').substringBefore(':'))
            model.setAuthorName(collectedLyrics[i].substringBefore(','))
            model.setSongName(collectedLyrics[i].substringAfter(',').substringBefore(';'))

            if (collectedLyrics[i].substringAfter(':') == "Easy") {
                model.setImage_drawables(myImageList[0])
            } else if (collectedLyrics[i].substringAfter(':') == "Medium") {
                model.setImage_drawables(myImageList[1])
            } else {
                model.setImage_drawables(myImageList[2])
            }

            list.add(model)
        }
        return list
    }

    override fun onStop() {
        super.onStop()

//        val prefs = getSharedPreferences("collected", Context.MODE_PRIVATE)
//        val editor = prefs.edit()
//        editor.putStringSet("collected", collectedLyrics.toMutableSet())
//        editor.apply()
    }

    override fun onStart() {
        super.onStart()

//        val prefs = getSharedPreferences("collected", Context.MODE_PRIVATE)
//        collectedLyrics = ArrayList(prefs.getStringSet("collected", null))

    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        this.intent = intent
//    }
}
