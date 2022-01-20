package com.techmedia.lyricfinder.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.techmedia.lyricfinder.R
import com.techmedia.lyricfinder.adapter.LyricsAdapter
import com.techmedia.lyricfinder.databinding.FragmentSavedLyricsBinding
import com.techmedia.lyricfinder.ui.LyricsActivity
import com.techmedia.lyricfinder.ui.LyricsViewModel

class SavedLyricsFragment : Fragment(R.layout.fragment_saved_lyrics) {
    private lateinit var viewModel: LyricsViewModel
    private lateinit var savedLyricsBinding: FragmentSavedLyricsBinding
    private lateinit var lyricsAdapter: LyricsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedLyricsBinding = FragmentSavedLyricsBinding.bind(view)
        viewModel = (activity as LyricsActivity).viewModel


        setupRecycleView()



        //        Swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val lyrics = lyricsAdapter.differ.currentList[position]
                viewModel.deleteLyrics(lyrics)
                Snackbar.make(view, "Lyrics deleted successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveLyrics(lyrics)
                    }
                    show()
                }

            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(savedLyricsBinding.savedLyricsRecyclerView)
        }

        viewModel.getSavedLyrics().observe(viewLifecycleOwner,
            { lyrics ->
                savedLyricsBinding.noData.isVisible = lyrics.isEmpty()
                lyricsAdapter.differ.submitList(lyrics)

            })
    }


    private fun setupRecycleView() {
        lyricsAdapter = LyricsAdapter()
        savedLyricsBinding.savedLyricsRecyclerView.apply {
            adapter = lyricsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}