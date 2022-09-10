package com.practice.galleriez.application

import android.app.Application
import com.practice.galleriez.di.AppComponent
import com.practice.galleriez.di.AppComponentImpl

/**
 * Created by loc.ta on 9/9/22.
 */
class App: Application() {

    val component: AppComponent by lazy { AppComponentImpl(this) }
}