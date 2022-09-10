package com.practice.galleriez.util

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import com.practice.galleriez.Constants
import com.practice.galleriez.domain.model.Media

/**
 * Created by loc.ta on 9/10/22.
 */
class MediaItemDiffCallback : DiffUtil.ItemCallback<Media>() {

    override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem.uri.toString() == newItem.uri.toString()
    }

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Media, newItem: Media): Any? {
        if (oldItem.uri.toString() == newItem.uri.toString()) {
            return if (oldItem.isFavorited == newItem.isFavorited) {
                super.getChangePayload(oldItem, newItem)
            } else {
                bundleOf(Constants.FAVORITE_PAYLOAD to newItem.isFavorited)
            }
        }
        return super.getChangePayload(oldItem, newItem)
    }
}