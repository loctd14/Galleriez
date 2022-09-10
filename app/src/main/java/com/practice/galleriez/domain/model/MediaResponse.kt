package com.practice.galleriez.domain.model

/**
 * Created by loc.ta on 9/10/22.
 */
data class MediaResponse(
    val media: List<Media>,
    val exhausted: Boolean,
    val lastColumnIndex: Int
) {

    companion object {

        val EMPTY = MediaResponse(emptyList(), true, 0)
    }
}