package com.ded.lyricsfinder

import android.app.Application

/*
    This class is used for storing and manging the data about collected lyrics
 */
class DataFinder : Application() {

    private var collectedLyrics = ArrayList<String>()

    fun getUpdatedHolder(): ArrayList<String> {
        return collectedLyrics
    }

    fun setNewHolder(newHolder: ArrayList<String>) {
        collectedLyrics = newHolder
    }

    fun removeEntity(lyric: String) {
        collectedLyrics.remove(lyric)
    }

    fun isHolderEmpty(): Boolean {
        return collectedLyrics.isEmpty()
    }

    fun clear() {
        collectedLyrics.clear()
    }

}