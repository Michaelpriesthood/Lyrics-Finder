package com.techmedia.lyricfinder.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.techmedia.lyricfinder.R
import com.techmedia.lyricfinder.databinding.FragmentHomeBinding
import com.techmedia.lyricfinder.model.LyricsModel
import com.techmedia.lyricfinder.ui.LyricsActivity
import com.techmedia.lyricfinder.ui.LyricsViewModel
import com.techmedia.lyricfinder.util.ScreenState


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var viewModel: LyricsViewModel
    private val args: HomeFragmentArgs by navArgs()
    private var lyrics: LyricsModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding = FragmentHomeBinding.bind(view)
        viewModel = (activity as LyricsActivity).viewModel
        homeBinding.songLyrics.movementMethod = ScrollingMovementMethod()


        homeBinding.btnGetSongLyrics.setOnClickListener {
            val artistName = homeBinding.artistName.text.toString()
            val songTitle = homeBinding.songTitle.text.toString()
            if (artistName.isEmpty() && songTitle.isEmpty()) {
                Snackbar.make(view, "artist and title cannot be empty", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.getLyricsData(artistName, songTitle)
            }
        }

        homeBinding.btnSaveLyrics?.setOnClickListener { saveLyrics() }



        viewModel.lyricsLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ScreenState.Success -> {
                    hideProgressBar()
                    response.data?.let { lyricsResponse ->
                        homeBinding.songLyrics.text = lyricsResponse.lyrics
                    }
                }

                is ScreenState.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "$message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is ScreenState.Loading -> {
                    showProgressBar()
                }
            }

        })
    }


    private fun saveLyrics() {
        if (homeBinding.songTitle.text.toString()
                .isEmpty() && homeBinding.songLyrics.text.toString().isEmpty()
        ) {
            view?.let {
                Snackbar.make(it, "You cannot save an empty field", Snackbar.LENGTH_SHORT)
                    .show()
            }
        } else {
            lyrics = args.lyrics
            when (lyrics) {
                null -> {
                    viewModel.saveLyrics(
                        LyricsModel(
                            0,
                            homeBinding.songLyrics.text.toString()
                        )
                    )
                    view?.let {
                        Snackbar.make(it, "Lyrics saved successfully", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    }


    private fun hideProgressBar() {
        homeBinding.loadingProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        homeBinding.loadingProgressBar.visibility = View.VISIBLE
    }


}
