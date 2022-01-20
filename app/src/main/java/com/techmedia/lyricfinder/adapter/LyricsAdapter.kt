package com.techmedia.lyricfinder.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.techmedia.lyricfinder.R
import com.techmedia.lyricfinder.databinding.LyricsItemBinding
import com.techmedia.lyricfinder.model.LyricsModel
import com.techmedia.lyricfinder.util.Constant

class LyricsAdapter : RecyclerView.Adapter<LyricsAdapter.LyricsViewModel>() {

    inner class LyricsViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemBinding = LyricsItemBinding.bind(itemView)
        val songLyrics: TextView = itemBinding.lyrics
        val btnViewLyrics: Button = itemBinding.btnViewFullLyrics

    }

    private val differCallback = object : DiffUtil.ItemCallback<LyricsModel>() {
        override fun areItemsTheSame(oldItem: LyricsModel, newItem: LyricsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LyricsModel, newItem: LyricsModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsViewModel {
        return LyricsViewModel(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lyrics_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LyricsViewModel, position: Int) {
        val lyrics = differ.currentList[position]
        holder.itemView.apply {
            holder.songLyrics.text = lyrics.lyrics
            holder.btnViewLyrics.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable(Constant.SERIAlIZABLE_KEY, lyrics.lyrics)
                }
                findNavController().navigate(
                    R.id.action_savedLyricsFragment_to_viewLyricsFragment,
                    bundle
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

