package com.techmedia.lyricfinder.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.techmedia.lyricfinder.R
import com.techmedia.lyricfinder.databinding.FragmentViewLyricsBinding
import com.techmedia.lyricfinder.ui.LyricsActivity
import com.techmedia.lyricfinder.ui.LyricsViewModel
import com.techmedia.lyricfinder.util.Constant


class ViewLyricsFragment : Fragment(R.layout.fragment_view_lyrics) {
    private lateinit var viewModel: LyricsViewModel
    private lateinit var navController: NavController
    private lateinit var viewLyricsBinding: FragmentViewLyricsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLyricsBinding = FragmentViewLyricsBinding.bind(view)
        navController = Navigation.findNavController(view)
        viewModel = (activity as LyricsActivity).viewModel
        viewLyricsBinding.fullLyrics.movementMethod = ScrollingMovementMethod()


        viewLyricsBinding.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        viewLyricsBinding.shareBtn.setOnClickListener { shareLyricsText() }

        viewLyricsBinding.fullLyrics.text = arguments?.getSerializable(Constant.SERIAlIZABLE_KEY).toString()

    }


    private fun shareLyricsText() {
        val lyricsText = viewLyricsBinding.fullLyrics.text.toString()
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, lyricsText)
        shareIntent.type = "text/plain"
        shareIntent = Intent.createChooser(shareIntent, "Share via:")
        startActivity(shareIntent)
    }

}