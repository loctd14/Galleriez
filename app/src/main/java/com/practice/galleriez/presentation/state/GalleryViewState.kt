package com.practice.galleriez.presentation.state

import android.net.Uri
import com.practice.galleriez.arch.CallBackEvent
import com.practice.galleriez.domain.model.Media

/**
 * Created by loc.ta on 9/10/22.
 */
sealed class GalleryIntent {

    object InitialLoad : GalleryIntent()

    class Favorite(val pos: Int) : GalleryIntent()

    class UpdateList(val media: List<Media>) : GalleryIntent(), CallBackEvent

    object LoadMore : GalleryIntent()

}

data class GalleryViewState(
    val media: List<Media>? = null,
    val cache: List<Uri>,
    val photoIndex: Int = 0,
    val arePhotosExhausted: Boolean = false,
    val videoIndex: Int = 0,
    val areVideosExhausted: Boolean = false
) {

    companion object {

        operator fun invoke(cache: List<Uri>): GalleryViewState {
            return GalleryViewState(cache = cache)
        }
    }
}