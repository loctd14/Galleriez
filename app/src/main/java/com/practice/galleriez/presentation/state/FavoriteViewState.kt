package com.practice.galleriez.presentation.state

import android.net.Uri
import com.practice.galleriez.domain.model.Media

/**
 * Created by loc.ta on 9/10/22.
 */
sealed class FavoriteIntent {

    object InitialLoad: FavoriteIntent()

    class UpdateFavorite(val item: Media): FavoriteIntent()
}

data class FavoriteViewState(
    val favorites: List<Media>? = null,
    val mapper: HashMap<Uri, Media> = HashMap()
) {

    companion object {

        val INITIAL_STATE = FavoriteViewState()
    }
}