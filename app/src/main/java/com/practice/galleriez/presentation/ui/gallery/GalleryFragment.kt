@file:OptIn(FlowPreview::class)

package com.practice.galleriez.presentation.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.galleriez.Constants
import com.practice.galleriez.arch.BaseMviFragment
import com.practice.galleriez.arch.CallBackEvent
import com.practice.galleriez.databinding.FragmentChildBinding
import com.practice.galleriez.di.GalleryComponent
import com.practice.galleriez.di.GalleryComponentImpl
import com.practice.galleriez.domain.model.Media
import com.practice.galleriez.domain.viewmodel.GalleryViewModel
import com.practice.galleriez.domain.viewmodel.GalleryViewModelFactory
import com.practice.galleriez.presentation.adapter.MediaAdapter
import com.practice.galleriez.presentation.ui.main.MainActivity
import com.practice.galleriez.presentation.state.GalleryIntent
import com.practice.galleriez.presentation.state.GalleryViewState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * Created by loc.ta on 9/10/22.
 */
class GalleryFragment :
    BaseMviFragment<FragmentChildBinding, GalleryIntent, GalleryViewState, GalleryViewModel>(
        FragmentChildBinding::inflate
    ), MediaAdapter.MediaActionListener {

    private val component: GalleryComponent by lazy { GalleryComponentImpl((activity as MainActivity).component) }

    private val adapter: MediaAdapter by lazy { MediaAdapter(MediaAdapter.Variant.ALBUM) }

    private val favoriteChannel = Channel<Int>(Channel.BUFFERED)

    private val loadMoreChannel = Channel<Boolean>(Channel.BUFFERED)

    private val layoutManager: GridLayoutManager by lazy { binding.recyclerviewData.layoutManager as GridLayoutManager }

    override val viewModel by activityViewModels<GalleryViewModel> {
        GalleryViewModelFactory(component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewData.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((layoutManager.findLastCompletelyVisibleItemPosition() >= layoutManager.itemCount - Constants.MIN_THRESHOLD) && dy > 0) {
                    loadMoreChannel.trySend(true)
                }
            }
        })
    }

    override suspend fun render(state: GalleryViewState) {}

    override fun viewEvents(): Flow<GalleryIntent> {
        val flows = listOf(
            flowOf(GalleryIntent.InitialLoad),
            favoriteChannel.consumeAsFlow()
                .map { GalleryIntent.Favorite(it) },
            loadMoreChannel.consumeAsFlow()
                .map { GalleryIntent.LoadMore }
        )
        return flows.asFlow().flattenMerge(flows.size)
    }

    override suspend fun executeCallBackEvent(event: CallBackEvent) {
        when (event) {
            is GalleryIntent.UpdateList -> {
                if (binding.recyclerviewData.adapter == null) {
                    binding.recyclerviewData.adapter = adapter
                    adapter.setOnMediaActionListener(this)
                }
                adapter.submitList(event.media)
            }
        }
    }

    override fun onFavorite(pos: Int, item: Media) {
        favoriteChannel.trySend(pos)
        val intent = Intent(Constants.MEDIA_INTENT_ACTION)
        intent.putExtra(Constants.MEDIA_EXTRA, item)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

}