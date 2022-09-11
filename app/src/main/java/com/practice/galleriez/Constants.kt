package com.practice.galleriez

/**
 * Created by loc.ta on 9/10/22.
 */
object Constants {

    const val FAVORITE_PAYLOAD = "fav_payload_extra"

    const val FAVORITE_PREFS = "fav_prefs"

    const val MEDIA_INTENT_ACTION = "media_intent_action"

    const val MEDIA_EXTRA = "media_extra"

    /**
     * Visible threshold before signaling another load more
     */
    const val MIN_THRESHOLD = 10

    /**
     * Change this value to increase/decrease the maximum number of media that can be displayed
     */
    const val MAX_THRESHOLD = 500

    /**
     * Number of media that can be loaded at a time
     */
    const val PAGE_SIZE = 10

    const val SEPARATOR = "*/*"

}
