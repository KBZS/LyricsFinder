package com.ded.lyricsfinder

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class LyricsAdapter (private val lyricsArrayList: ArrayList<LyricsPiece>) : RecyclerView.Adapter<LyricsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lyric = lyricsArrayList[position]


        holder.imgView.setImageResource(lyric.getImage_drawables())
        holder.txtMsg.setText(lyric.getLyrics())
        holder.txtMsg.setOnClickListener {v ->

            // Make new activity / or dialog box where player puts the name of the song
            val snackbar = Snackbar.make(v, "You used a coin booster for 15 minutes!", Snackbar.LENGTH_LONG)
            snackbar.show()

        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.row_layout, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lyricsArrayList.size
    }

    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var imgView: ImageView
        var txtMsg: TextView

        init {
            imgView = layout.findViewById<View>(R.id.diff_icon) as ImageView
            txtMsg = layout.findViewById<View>(R.id.lyrics_txt) as TextView
        }
    }

}