package com.practice.galleriez.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Created by loc.ta on 9/10/22.
 */
fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}

fun View.updateMargin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        left?.run { leftMargin = this }
        top?.run { topMargin = this }
        right?.run { rightMargin = this }
        bottom?.run { bottomMargin = this }
    }
}