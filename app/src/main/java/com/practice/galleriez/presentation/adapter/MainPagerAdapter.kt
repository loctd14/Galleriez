package com.practice.galleriez.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practice.galleriez.presentation.ui.favorite.FavoriteFragment
import com.practice.galleriez.presentation.ui.gallery.GalleryFragment

/**
 * Created by loc.ta on 9/10/22.
 */
class MainPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GalleryFragment()
            1 -> FavoriteFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }

}