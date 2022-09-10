package com.practice.galleriez.di

import com.practice.galleriez.domain.usecase.FavoriteUseCase
import com.practice.galleriez.domain.usecase.PhotoUseCase
import com.practice.galleriez.domain.usecase.VideoUseCase

/**
 * Created by loc.ta on 9/10/22.
 */
interface GalleryComponent {

    val photoUseCase: PhotoUseCase

    val videoUseCase: VideoUseCase

    val favoriteUseCase: FavoriteUseCase
}

class GalleryComponentImpl(appComponent: AppComponent) : GalleryComponent,
    AppComponent by appComponent {

    override val photoUseCase: PhotoUseCase by lazy { PhotoUseCase(appComponent.contentResolver) }

    override val videoUseCase: VideoUseCase by lazy { VideoUseCase(appComponent.contentResolver) }

    override val favoriteUseCase: FavoriteUseCase by lazy {
        FavoriteUseCase(
            appComponent.contentResolver,
            appComponent.sharedPreferences
        )
    }
}