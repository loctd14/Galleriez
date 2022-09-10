package com.practice.galleriez.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import com.practice.galleriez.BuildConfig

/**
 * Created by loc.ta on 9/10/22.
 */
interface AppComponent {

    val app: Application

    val contentResolver: ContentResolver

    val sharedPreferences: SharedPreferences
}

class AppComponentImpl(override val app: Application) : AppComponent {

    override val contentResolver: ContentResolver by lazy { app.contentResolver }

    override val sharedPreferences: SharedPreferences by lazy {
        app.applicationContext.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )
    }

}