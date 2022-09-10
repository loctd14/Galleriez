package com.practice.galleriez.domain.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.practice.galleriez.arch.BaseViewModel
import com.practice.galleriez.di.GalleryComponent
import com.practice.galleriez.domain.model.MediaResponse
import com.practice.galleriez.presentation.state.GalleryIntent
import com.practice.galleriez.presentation.state.GalleryViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by loc.ta on 9/10/22.
 */
class GalleryViewModel(private val component: GalleryComponent) :
    BaseViewModel<GalleryIntent, GalleryViewState>() {

    init {
        mutableStateFlow = MutableStateFlow(
            GalleryViewState.invoke(component.favoriteUseCase.getAsUri())
        )
    }

    override suspend fun bind(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.Favorite -> {
                viewModelScope.launch(Dispatchers.Default) {
                    state.value.media?.let { media ->
                        val item = media[intent.pos]
                        val updated = media.run {
                            toMutableList().apply {
                                set(intent.pos, item.setFavorite(!item.isFavorited))
                            }
                        }
                        mutableStateFlow.update { it.copy(media = updated) }
                    }
                }.invokeOnCompletion {
                    postEvent({ GalleryIntent.UpdateList(state.value.media.orEmpty()) })
                }
            }

            is GalleryIntent.InitialLoad,
            is GalleryIntent.LoadMore -> {
                if (state.value.arePhotosExhausted && state.value.areVideosExhausted) return
                viewModelScope.launch {
                    val photos = async {
                        if (state.value.arePhotosExhausted) {
                            flowOf(MediaResponse.EMPTY)
                        } else {
                            fetchPhotos(state.value.cache)
                        }
                    }
                    val videos = async {
                        if (state.value.areVideosExhausted) {
                            flowOf(MediaResponse.EMPTY)
                        } else {
                            fetchVideos(state.value.cache)
                        }
                    }
                    combine(photos.await(), videos.await()) { p, v ->
                        val media = p.media + v.media
                        mutableStateFlow.update {
                            it.copy(
                                media = if (it.media == null) {
                                    media
                                } else {
                                    it.media.run { toMutableList().apply { addAll(media) } }
                                },
                                photoIndex = p.lastColumnIndex,
                                arePhotosExhausted = p.exhausted,
                                videoIndex = v.lastColumnIndex,
                                areVideosExhausted = v.exhausted
                            )
                        }
                    }.collect()
                }.invokeOnCompletion {
                    postEvent({ GalleryIntent.UpdateList(state.value.media.orEmpty()) })
                }
            }

            else -> {
                /*Do nothing*/
            }
        }
    }

    private fun fetchPhotos(cache: List<Uri>): Flow<MediaResponse> {
        return flow {
            val res = component.photoUseCase.fetchPhotos(state.value.photoIndex)
            if (cache.isEmpty()) {
                emit(res)
            } else {
                emit(
                    res.copy(
                        media = res.media.map {
                            if (cache.indexOf(it.uri) != -1) {
                                it.setFavorite(true)
                            } else {
                                it
                            }
                        }
                    )
                )
            }
        }
    }

    private fun fetchVideos(cache: List<Uri>): Flow<MediaResponse> {
        return flow {
            val res = component.videoUseCase.fetchVideos(state.value.videoIndex)
            if (cache.isEmpty()) {
                emit(res)
            } else {
                emit(
                    res.copy(
                        media = res.media.map {
                            if (cache.indexOf(it.uri) != -1) {
                                it.setFavorite(true)
                            } else {
                                it
                            }
                        }
                    )
                )
            }
        }
    }
}