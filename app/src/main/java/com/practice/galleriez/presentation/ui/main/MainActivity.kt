package com.practice.galleriez.presentation.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.practice.galleriez.application.App
import com.practice.galleriez.databinding.ActivityMainBinding
import com.practice.galleriez.extension.getStatusBarHeight
import com.practice.galleriez.extension.updateMargin
import com.practice.galleriez.presentation.adapter.MainPagerAdapter

/**
 * Created by loc.ta on 9/9/22.
 */
class MainActivity : AppCompatActivity() {

    val component by lazy { (application as App).component }

    private var _binding: ViewBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding as ActivityMainBinding

    companion object {

        private const val GALLERY = 0

        private const val FAVORITE = 1

        private const val REQUEST_CODE = 9009
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spaceDummy.updateMargin(top = getStatusBarHeight(this))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            binding.viewpager.visibility = View.VISIBLE
            setupViewPager()
            setupViews()
        } else {
            binding.linearLayoutPermission.visibility = View.VISIBLE
            binding.buttonGrantAccess.setOnClickListener {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.viewpager.visibility = View.VISIBLE
                binding.linearLayoutPermission.visibility = View.GONE
                setupViewPager()
                setupViews()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewPager() {
        binding.viewpager.offscreenPageLimit = 1
        binding.viewpager.isUserInputEnabled = false
        binding.viewpager.adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
    }

    private fun toggleGallery() {
        if (binding.checkboxPhoto.isChecked) return
        binding.checkboxPhoto.isChecked = true
        binding.checkboxFavorite.isChecked = false
        binding.viewpager.setCurrentItem(GALLERY, true)
    }

    private fun toggleFavorite() {
        if (binding.checkboxFavorite.isChecked) return
        binding.checkboxFavorite.isChecked = true
        binding.checkboxPhoto.isChecked = false
        binding.viewpager.setCurrentItem(FAVORITE, true)
    }

    private fun setupViews() {
        binding.checkboxPhoto.isChecked = true
        binding.cardViewGallery.setOnClickListener { toggleGallery() }
        binding.cardViewFavorite.setOnClickListener { toggleFavorite() }
    }
}