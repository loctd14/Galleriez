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
class VideoUseCase(private val resolver: ContentResolver) {

    private val externalUri by lazy { MediaStore.Video.Media.EXTERNAL_CONTENT_URI }

    private val projection by lazy { arrayOf(MediaStore.Video.VideoColumns._ID) }

    private val cursor by lazy { resolver.query(externalUri, projection, null, null, null) }

    suspend fun fetchVideos(start: Int): MediaResponse {
        val videos = mutableListOf<Media>()
        var index = start
        val columnIndexID: Int
        var videoId: Long
        var isExhausted = false
        return withContext(Dispatchers.IO) {
            cursor?.let {
                columnIndexID = it.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID)
                while (it.moveToPosition(index)) {
                    videoId = it.getLong(columnIndexID)
                    val uri = Uri.withAppendedPath(externalUri, "$videoId")
                    index++
                    videos.add(Media.Video(uri))
                    if (videos.size == Constants.PAGE_SIZE) break
                }
                isExhausted = index >= Constants.MAX_THRESHOLD
            }
            if (isExhausted) {
                cursor?.close()
            }
            MediaResponse(videos, isExhausted, index)
        }
    }

}