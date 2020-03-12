package com.ded.lyricsfinder

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_song_guesser.*

class MyAdapter (private val imageModelArrayList: MutableList<MyModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var diffIcon: ImageView = layout.findViewById<View>(R.id.diff_icon) as ImageView
        var lyricsTxt: TextView = layout.findViewById<View>(R.id.lyrics_txt) as TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = imageModelArrayList[position]

        holder.diffIcon.setImageResource(info.getImage_drawables())
        holder.lyricsTxt.text = info.getLyrics()

        holder.lyricsTxt.setOnClickListener {v ->

            val myDialog = Dialog(v.rootView.context)
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            myDialog.setContentView(R.layout.activity_song_guesser)

            // Number of tries available before removal of the lyrics piece
            var nTries = 4

            myDialog.guess_btn.setOnClickListener {
                var authorInput = myDialog.author_in.text.toString().toLowerCase()
                var nameInput = myDialog.name_in.text.toString().toLowerCase()
                Log.e("AUTHOR", info.getAuthorName())
                Log.e("NAME", info.getSongName())

                if (info.getAuthorName() == authorInput && info.getSongName() == nameInput) {
                    Log.e("answer", "correct")

                    myDialog.cancel()
                    var message = ""
                    val winDialog = androidx.appcompat.app.AlertDialog.Builder(v.context)

                    val prefs = v.context.getSharedPreferences("gameResources", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    if (info.getDifficulty() == "Easy") {
                        message = "Correct! You got 100 coins and 50 XP."
                        editor.putInt("coins", prefs.getInt("coins", 0) + 100)
                        editor.putInt("xp", 50)
                    } else if (info.getDifficulty() == "Medium") {
                        message = "Correct! You got 150 coins and 60 XP."
                        editor.putInt("coins", prefs.getInt("coins", 0) + 150)
                        editor.putInt("xp", 60)
                    } else {
                        message = "Correct! You got 200 coins and 70 XP."
                        editor.putInt("coins", prefs.getInt("coins", 0) + 200)
                        editor.putInt("xp", 70)
                    }
                    winDialog.setMessage(message)
                    editor.putInt("lvl", prefs.getInt("lvl", 4) + 1)
                    editor.apply()

                    winDialog.setNegativeButton("Nice") { _, _ ->
                        val prefs1 = v.context.getSharedPreferences("generalLyrics", Context.MODE_PRIVATE)
                        val editor1 = prefs1.edit()
                        val guess: String = info.getAuthorName() + ',' + info.getSongName() + ";" + info.getLyrics() + ":" + info.getDifficulty()
                        editor1.putString("isSolved", guess)
                        editor1.putBoolean("isChanged", true)
                        editor1.apply()
                    }
                    winDialog.show()

                } else {
                    Log.e("answer", "incorrect")
                    Log.e("answerTRUE", info.getAuthorName())

                    val loseDialog = androidx.appcompat.app.AlertDialog.Builder(v.context)
                    if (nTries > 1) {
                        loseDialog.setMessage("Incorrect! You have $nTries tries left.")
                        nTries --
                        loseDialog.setNegativeButton("OK") { _, _ -> }
                    } else if (nTries == 1) {
                        loseDialog.setMessage("Incorrect! You have only $nTries try left.")
                        nTries --
                        loseDialog.setNegativeButton("OK") { _, _ -> }
                    } else {
                        loseDialog.setMessage("You do not have any tries left. Lyrics are deleted. You will not receive any rewards.")
                        loseDialog.setNegativeButton("OK") { _, _ ->
                            myDialog.cancel()
                            val prefs = v.context.getSharedPreferences("generalLyrics", Context.MODE_PRIVATE)
                            val editor = prefs.edit()
                            val guess: String = info.getAuthorName() + ',' + info.getSongName() + ";" + info.getLyrics() + ":" + info.getDifficulty()
                            editor.putString("isSolved", guess)
                            editor.putBoolean("isChanged", true)
                            editor.apply()
                        }
                    }
                    loseDialog.show()
                }
            }

            myDialog.give_up_btn.setOnClickListener {
                val giveUp = androidx.appcompat.app.AlertDialog.Builder(v.context)
                giveUp.setMessage("You gave up. The song is: " + info.getSongName()
                        + " by " + info.getAuthorName() + ". You will not receive any rewards.")
                giveUp.setNegativeButton("OK") { _, _ ->
                    myDialog.cancel()
                    val prefs = v.context.getSharedPreferences("generalLyrics", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    val guess: String =
                        info.getAuthorName() + ',' + info.getSongName() + ";" + info.getLyrics() + ":" + info.getDifficulty()
                    editor.putString("isSolved", guess)
                    editor.putBoolean("isChanged", true)
                    editor.apply()
                }
                giveUp.show()
            }

            myDialog.show()
        }
    }


    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }
}