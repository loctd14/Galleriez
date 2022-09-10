package com.practice.galleriez.extension

import android.content.SharedPreferences
import com.practice.galleriez.Constants

/**
 * Created by loc.ta on 9/10/22.
 */
inline fun SharedPreferences.editor(block: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    block(editor)
    editor.apply()
}

var SharedPreferences.favorites: String
    get() = getString(Constants.FAVORITE_PREFS, null) ?: ""
    set(value) {
        editor {
            it.putString(Constants.FAVORITE_PREFS, value)
        }
    }
