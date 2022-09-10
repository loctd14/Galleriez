package com.practice.galleriez.application

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Build
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Created by loc.ta on 9/9/22.
 */
@GlideModule
class CustomGlideApp : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(RequestOptions().format(getBitmapQuality(context)))
    }

    private fun getBitmapQuality(context: Context): DecodeFormat {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || isLowMemory(context)) {
            DecodeFormat.PREFER_RGB_565
        } else {
            DecodeFormat.PREFER_ARGB_8888
        }
    }

    private fun isLowMemory(context: Context): Boolean {
        val activityManager: ActivityManager? =
            context.getSystemService(ACTIVITY_SERVICE) as? ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()

        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo)
            return memoryInfo.lowMemory
        }
        return true
    }
}