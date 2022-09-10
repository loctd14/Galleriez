package com.practice.galleriez.di

import com.practice.galleriez.domain.usecase.FavoriteUseCase

/**
 * Created by loc.ta on 9/10/22.
 */
interface FavoriteComponent {

    val favoriteUseCase: FavoriteUseCase
}

class FavoriteComponentImpl(appComponent: AppComponent) : FavoriteComponent,
    AppComponent by appComponent {

    override val favoriteUseCase: FavoriteUseCase by lazy {
        FavoriteUseCase(
            appComponent.contentResolver,
            appComponent.sharedPreferences
        )
    }
}