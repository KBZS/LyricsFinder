package com.ded.lyricsfinder

//data class LyricsPiece(val lyricsText: String, val image: String)

class LyricsPiece {
    var lyricsText: String? = null
    var image_drawable: Int = 0

    fun getLyrics(): String {
        return lyricsText.toString()
    }

    fun setLyrics(lyricsText: String) {
        this.lyricsText = lyricsText
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }

    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }
}