package com.practice.galleriez.arch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.practice.galleriez.extension.collectIn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by loc.ta on 9/10/22.
 */
abstract class BaseMviFragment<VB : ViewBinding, Intent, State, ViewModel : BaseViewModel<Intent, State>>(
    inflater: FragmentInflater<VB>
) : BaseFragment<VB>(inflater), ViewEventFlow<Intent> {

    protected abstract val viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state
            .collectIn(this) { render(it) }

        viewModel.events
            .collectIn(this) { events ->
                events.forEach { executeCallBackEvent(it.invoke()) }
            }

        viewEvents()
            .onEach { intent -> viewModel.intentChannel.send(intent) }
            .launchIn(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    protected abstract suspend fun render(state: State)

    open suspend fun executeCallBackEvent(event: CallBackEvent) {}
}