package com.practice.galleriez.domain.usecase

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.practice.galleriez.Constants
import com.practice.galleriez.domain.model.Media
import com.practice.galleriez.domain.model.MediaResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by loc.ta on 9/10/22.
 */
class PhotoUseCase(private val resolver: ContentResolver) {

    private val externalUri by lazy { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }

    private val projection by lazy { arrayOf(MediaStore.Images.Media._ID) }

    private val cursor by lazy { resolver.query(externalUri, projection, null, null, null) }

    suspend fun fetchPhotos(start: Int): MediaResponse {
        val photos = mutableListOf<Media>()
        var index = start
        val columnIndexID: Int
        var imageId: Long
        var isExhausted = false
        return withContext(Dispatchers.IO) {
            cursor?.let {
                columnIndexID = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (it.moveToPosition(index)) {
                    imageId = it.getLong(columnIndexID)
                    val uri = Uri.withAppendedPath(externalUri, "$imageId")
                    index++
                    photos.add(Media.Photo(uri))
                    if (photos.size == Constants.PAGE_SIZE) break
                }
                isExhausted = index >= Constants.MAX_THRESHOLD
            }
            if (isExhausted) {
                cursor?.close()
            }
            MediaResponse(photos, isExhausted, index)
        }
    }
}