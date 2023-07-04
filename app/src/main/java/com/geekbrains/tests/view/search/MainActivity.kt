package com.geekbrains.tests.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.tests.R
import com.geekbrains.tests.databinding.ActivityMainBinding
import com.geekbrains.tests.model.ScreenState
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.view.details.DetailsActivity
import java.util.Locale

class MainActivity : AppCompatActivity(), ViewSearchContract {

    private val adapter = SearchResultAdapter()
    private var totalCount: Int = 0
    private val viewModel : SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    private lateinit var binding: ActivityMainBinding
    val _binding get() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        viewModel.subscribeToLiveData().observe(this){
            onStateChange(it)
        }
    }

    private fun onStateChange(screenState: ScreenState) {
        when(screenState) {
            is ScreenState.Working -> {
                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                binding.progressBar.visibility = View.GONE
                with(binding.totalCountTextView) {
                    visibility = View.VISIBLE
                    text = String.format(
                        Locale.getDefault(),
                        getString(R.string.results_count),
                        totalCount
                    )
                }
                this.totalCount = totalCount!!
                adapter.updateResults(searchResponse.searchResults!!)
            }

            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this,
                screenState.error.message, Toast.LENGTH_SHORT).show()
            }

            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotBlank()) {
                viewModel.searchGitHub(query)
            } else {
                showEmptyTextToastMessage()
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setQueryListener() {
        binding.searchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchGitHub(query)
                    return@OnEditorActionListener true
                } else {
                    showEmptyTextToastMessage()
                    return@OnEditorActionListener false
                }
            }
            false
        })
    }

    override fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    ) {
        with(binding.totalCountTextView){
            visibility = View.VISIBLE
            text = String.format(Locale.getDefault(),getString(R.string.results_count), totalCount)
        }

        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    private fun showEmptyTextToastMessage() {
        Toast.makeText(
            this@MainActivity,
            getString(R.string.enter_search_word),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
