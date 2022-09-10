package com.practice.galleriez.domain.viewmodel

import androidx.lifecycle.viewModelScope
import com.practice.galleriez.arch.BaseViewModel
import com.practice.galleriez.di.FavoriteComponent
import com.practice.galleriez.presentation.state.FavoriteIntent
import com.practice.galleriez.presentation.state.FavoriteViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by loc.ta on 9/10/22.
 */
class FavoriteViewModel(private val component: FavoriteComponent) :
    BaseViewModel<FavoriteIntent, FavoriteViewState>() {

    init {
        mutableStateFlow = MutableStateFlow(FavoriteViewState.INITIAL_STATE)
    }

    override suspend fun bind(intent: FavoriteIntent) {
        when (intent) {
            is FavoriteIntent.InitialLoad -> {
                val cache = component.favoriteUseCase.get()
                if (cache.isNotEmpty()) {
                    val size = cache.size
                    mutableStateFlow.update {
                        it.copy(
                            favorites = cache,
                            mapper = cache.associateByTo(HashMap(size)) { media -> media.uri }
                        )
                    }
                } else {
                    mutableStateFlow.update { it.copy(favorites = cache) }
                }
            }

            is FavoriteIntent.UpdateFavorite -> {
                val media = intent.item
                val cache = state.value.mapper[media.uri]
                viewModelScope.launch(Dispatchers.Default) {
                    if (cache != null) {
                        mutableStateFlow.update {
                            it.copy(
                                favorites = state.value.favorites?.run {
                                    toMutableList().apply {
                                        remove(cache)
                                    }.also { updated -> component.favoriteUseCase.update(updated) }
                                },
                                mapper = state.value.mapper.apply {
                                    remove(media.uri)
                                }
                            )
                        }
                    } else {
                        component.favoriteUseCase.add(media)
                        mutableStateFlow.update {
                            it.copy(
                                favorites = state.value.favorites?.run {
                                    toMutableList().apply {
                                        add(media)
                                    }
                                },
                                mapper = state.value.mapper.apply {
                                    put(media.uri, media)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}