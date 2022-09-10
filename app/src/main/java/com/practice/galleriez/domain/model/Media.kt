package com.practice.galleriez.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by loc.ta on 9/10/22.
 */
sealed class Media: Parcelable {

    abstract val uri: Uri

    abstract val isFavorited: Boolean

    abstract fun setFavorite(isFavorited: Boolean) : Media

    @Parcelize
    data class Photo(override val uri: Uri, override val isFavorited: Boolean = false) : Media() {

        override fun setFavorite(isFavorited: Boolean): Media {
            return copy(isFavorited = isFavorited)
        }
    }

    @Parcelize
    data class Video(override val uri: Uri, override val isFavorited: Boolean = false) : Media() {

        override fun setFavorite(isFavorited: Boolean): Media {
            return copy(isFavorited = isFavorited)
        }
    }

}