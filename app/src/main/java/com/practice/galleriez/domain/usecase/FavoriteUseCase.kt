package com.practice.galleriez.domain.usecase

import android.content.ContentResolver
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.net.toUri
import com.practice.galleriez.Constants
import com.practice.galleriez.domain.model.Media
import com.practice.galleriez.extension.favorites
import java.io.IOException

/**
 * Created by loc.ta on 9/10/22.
 */
class FavoriteUseCase(
    private val resolver: ContentResolver,
    private val sharedPreferences: SharedPreferences
) {

    fun update(media: List<Media>) {
        sharedPreferences.favorites =
            media.joinToString(separator = Constants.SEPARATOR) { it.uri.toString() }
    }

    fun add(item: Media) {
        val sb = StringBuilder(sharedPreferences.favorites)
        sb.append(item.uri.toString())
        sb.append(Constants.SEPARATOR)
        sharedPreferences.favorites = sb.toString()
    }

    fun get(): List<Media> {
        val favorites = sharedPreferences.favorites
        if (favorites.isEmpty()) return emptyList()
        return favorites
            .split(Constants.SEPARATOR)
            .asSequence()
            .map { it.toUri() }
            .filter { it.isExist() }
            .map {
                if (it.path?.contains("video") == true) {
                    Media.Video(it, true)
                } else {
                    Media.Photo(it, true)
                }
            }
            .toList()
    }

    fun getAsUri(): List<Uri> {
        val favorites = sharedPreferences.favorites
        if (favorites.isEmpty()) return emptyList()
        return favorites
            .split(Constants.SEPARATOR)
            .asSequence()
            .map { it.toUri() }
            .toList()
    }

    private fun Uri.isExist(): Boolean {
        return try {
            resolver.openInputStream(this)?.use {}
            true
        } catch (e: IOException) {
            false
        }
    }
}