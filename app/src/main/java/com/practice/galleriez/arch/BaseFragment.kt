package com.practice.galleriez.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias FragmentInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Created by loc.ta on 9/9/2022.
 */
abstract class BaseFragment<VB : ViewBinding>(private val fragmentInflater: FragmentInflater<VB>) :
    Fragment() {

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = fragmentInflater.invoke(inflater, container, false)
        return binding.root
    }

}