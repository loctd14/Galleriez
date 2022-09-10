@file:OptIn(FlowPreview::class)

package com.practice.galleriez.presentation.ui.favorite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.practice.galleriez.Constants
import com.practice.galleriez.arch.BaseMviFragment
import com.practice.galleriez.databinding.FragmentChildBinding
import com.practice.galleriez.di.FavoriteComponent
import com.practice.galleriez.di.FavoriteComponentImpl
import com.practice.galleriez.domain.model.Media
import com.practice.galleriez.domain.viewmodel.FavoriteViewModel
import com.practice.galleriez.domain.viewmodel.FavoriteViewModelFactory
import com.practice.galleriez.presentation.adapter.MediaAdapter
import com.practice.galleriez.presentation.ui.main.MainActivity
import com.practice.galleriez.presentation.state.FavoriteIntent
import com.practice.galleriez.presentation.state.FavoriteViewState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * Created by loc.ta on 9/10/22.
 */
class FavoriteFragment :
    BaseMviFragment<FragmentChildBinding, FavoriteIntent, FavoriteViewState, FavoriteViewModel>(
        FragmentChildBinding::inflate
    ) {

    private val component: FavoriteComponent by lazy {
        FavoriteComponentImpl((activity as MainActivity).component)
    }

    private val updateChannel = Channel<Media>(Channel.BUFFERED)

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.hasExtra(Constants.MEDIA_EXTRA)) {
                val media = intent.getParcelableExtra<Media?>(Constants.MEDIA_EXTRA)
                if (media != null) {
                    updateChannel.trySend(media)
                }
            }
        }
    }

    private val adapter by lazy { MediaAdapter(MediaAdapter.Variant.FAVORITE) }

    override val viewModel by activityViewModels<FavoriteViewModel> {
        FavoriteViewModelFactory(component)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, IntentFilter(Constants.MEDIA_INTENT_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
    }

    override suspend fun render(state: FavoriteViewState) {
        if (state.favorites != null) {
            if (binding.recyclerviewData.adapter == null) {
                binding.recyclerviewData.adapter = adapter
            }
            adapter.submitList(state.favorites)
        }
    }

    override fun viewEvents(): Flow<FavoriteIntent> {
        val flows = listOf(
            flowOf(FavoriteIntent.InitialLoad),
            updateChannel.consumeAsFlow()
                .map { FavoriteIntent.UpdateFavorite(it) }
        )
        return flows.asFlow().flattenMerge(flows.size)
    }

}