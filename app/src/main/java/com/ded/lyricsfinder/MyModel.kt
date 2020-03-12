package com.ded.lyricsfinder

class MyModel {
    var song: String? = null
    var author: String? = null
    var lyric: String? = null
    var image_drawable: Int = 0

    fun getSongName(): String {
        return song.toString()
    }
    fun setSongName(song: String) {
        this.song = song
    }

    fun getAuthorName(): String {
        return author.toString()
    }
    fun setAuthorName(author: String) {
        this.author = author
    }

    fun getLyrics(): String {
        return lyric.toString()
    }
    fun setLyrics(author: String) {
        this.lyric = author
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }
    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }

    fun getDifficulty(): String {
        var diff = ""
        if (getImage_drawables() == 2131230855) {
            diff = "Easy"
        } else if (getImage_drawables() == 2131230910) {
            diff = "Medium"
        } else diff = "Hard"
        return diff
    }
}