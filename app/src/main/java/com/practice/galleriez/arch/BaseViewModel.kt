package com.practice.galleriez.arch

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by loc.ta on 9/9/2022.
 */
abstract class BaseViewModel<Intent, State> : ViewModel() {

    protected lateinit var mutableStateFlow: MutableStateFlow<State>

    private val _events =
        MutableSharedFlow<Array<out () -> CallBackEvent>>(extraBufferCapacity = 64)

    val events: SharedFlow<Array<out () -> CallBackEvent>>
        get() = _events

    private val callBackChannel =
        Channel<Array<out () -> CallBackEvent>>(Channel.UNLIMITED)

    val intentChannel = Channel<Intent>(Channel.UNLIMITED)

    val state: StateFlow<State>
        get() = mutableStateFlow

    init {
        viewModelScope.launch {
            intentChannel
                .consumeAsFlow()
                .collect { intent -> bind(intent) }
        }
    }

    protected fun postEvent(vararg block: () -> CallBackEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            _events.emit(block)
        }
    }

    protected abstract suspend fun bind(intent: Intent)

    @CallSuper
    open fun onPause() {}

}