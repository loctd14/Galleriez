package com.practice.galleriez.arch

import kotlinx.coroutines.flow.Flow

/**
 * Created by loc.ta on 9/9/2022.
 */
interface ViewEventFlow<I> {

    fun viewEvents(): Flow<I>
}
