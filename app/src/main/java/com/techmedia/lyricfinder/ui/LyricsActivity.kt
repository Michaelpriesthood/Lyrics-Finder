package com.techmedia.lyricfinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.techmedia.lyricfinder.R
import com.techmedia.lyricfinder.databinding.ActivityLyricsBinding
import com.techmedia.lyricfinder.db.LyricsDatabase
import com.techmedia.lyricfinder.repository.LyricsRepository

class LyricsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLyricsBinding
    lateinit var viewModel: LyricsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lyricsRepository = LyricsRepository(LyricsDatabase(this))
        val viewModelProviderFactory = LyricsViewModelProviderFactory(application, lyricsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[LyricsViewModel::class.java]


        //        Setting the BottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.lyrics_navHost_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

    }
}