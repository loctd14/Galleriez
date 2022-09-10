package com.practice.galleriez.presentation.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.galleriez.Constants
import com.practice.galleriez.R
import com.practice.galleriez.application.GlideApp
import com.practice.galleriez.databinding.ItemPhotoBinding
import com.practice.galleriez.domain.model.Media
import com.practice.galleriez.util.MediaItemDiffCallback

/**
 * Created by loc.ta on 9/10/22.
 */
class MediaAdapter(private val variant: Variant): ListAdapter<Media, MediaAdapter.MediaViewHolder>(
    AsyncDifferConfig.Builder(MediaItemDiffCallback()).build()
) {

    enum class Variant {
        ALBUM,
        FAVORITE
    }

    interface MediaActionListener {

        fun onFavorite(pos: Int, item: Media)
    }

    private var listener: MediaActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding =  ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(
        holder: MediaViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty() || payloads[0] !is Bundle) {
            holder.bind(getItem(position))
        } else {
            holder.update(payloads[0] as Bundle)
        }
    }

    fun setOnMediaActionListener(l: MediaActionListener?) {
        listener = l
    }

    inner class MediaViewHolder(binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {

        private val image = binding.imageviewPhoto

        private val frame = binding.frameLayoutVideo

        private val favorite = binding.imageviewHeart

        private val container = binding.cardViewHeart

        init {
            favorite.setOnClickListener {
                listener?.onFavorite(bindingAdapterPosition, getItem(bindingAdapterPosition))
            }
        }

        fun bind(media: Media) {
            when (media) {
                is Media.Photo -> {
                    frame.visibility = View.GONE
                }
                is Media.Video -> {
                    frame.visibility = View.VISIBLE
                }
            }
            GlideApp.with(image.context)
                .load(media.uri)
                .into(image)

            when (variant) {
                Variant.ALBUM -> {
                    container.visibility = View.VISIBLE
                    setFavorite(media.isFavorited)
                }
                Variant.FAVORITE -> {
                    container.visibility = View.GONE
                }
            }
        }

        fun update(bundle: Bundle) {
            if (bundle.containsKey(Constants.FAVORITE_PAYLOAD)) {
                val fav = bundle.getBoolean(Constants.FAVORITE_PAYLOAD)
                setFavorite(fav)
            }
        }

        private fun setFavorite(fav: Boolean) {
            favorite.setImageResource(
                if (fav) {
                    R.drawable.ic_favorite_filled
                } else {
                    R.drawable.ic_favorite_outline
                }
            )
        }
    }
}