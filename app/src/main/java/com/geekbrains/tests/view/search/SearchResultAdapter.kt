package com.geekbrains.tests.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.tests.databinding.ListItemBinding
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.view.search.SearchResultAdapter.SearchResultViewHolder

internal class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

    private var results: List<SearchResult> = listOf()

    fun getData(): List<SearchResult> {
        return this.results
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchResultViewHolder,
        position: Int
    ) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun updateResults(results: List<SearchResult>) {
        this.results = results
        notifyDataSetChanged()
    }

    internal class SearchResultViewHolder(
       private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchResult: SearchResult) {
            with(binding) {
                tvId.text = searchResult.id?.toString() ?: "N/A"
                tvName.text = searchResult.name ?: "N/A"
                tvFullName.text = searchResult.fullName ?: "N/A"
                tvDescription.text = searchResult.description ?: "N/A"
                tvUpdatedAt.text = searchResult.updatedAt ?: "N/A"
                tvSize.text = searchResult.size?.toString() ?: "N/A"
                tvStargazersCount.text = searchResult.stargazersCount?.toString() ?: "N/A"
                tvLanguage.text = searchResult.language ?: "N/A"
                tvScore.text = searchResult.score?.toString() ?: "N/A"
            }

            binding.root.setOnClickListener {
                Toast.makeText(itemView.context, searchResult.fullName, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
