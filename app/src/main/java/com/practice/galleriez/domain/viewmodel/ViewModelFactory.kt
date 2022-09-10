@file:Suppress("UNCHECKED_CAST")

package com.practice.galleriez.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.galleriez.di.FavoriteComponent
import com.practice.galleriez.di.GalleryComponent

/**
 * Created by loc.ta on 9/10/22.
 */
internal class GalleryViewModelFactory(private val component: GalleryComponent) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(component) as T
    }

}

internal class FavoriteViewModelFactory(private val component: FavoriteComponent) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(component) as T
    }

}