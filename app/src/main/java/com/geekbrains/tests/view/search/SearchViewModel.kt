package com.geekbrains.tests.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.tests.model.ScreenState
import com.geekbrains.tests.presenter.SchedulerProvider
import com.geekbrains.tests.presenter.search.SearchSchedulerProvider
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.repository.NetworkModule
import com.geekbrains.tests.repository.RepositoryContract
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        NetworkModule.createRetrofit().create(GitHubApi::class.java)
    ),
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : ViewModel() {

    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    private fun handleError(error: Throwable) {
        _liveData.value = ScreenState.Error(
            Throwable(error.message ?: "Response is null or unsuccessful")
        )
    }

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) })

    fun subscribeToLiveData() = liveData

    fun searchGitHub(searchQuery: String) {
        _liveData.value = ScreenState.Loading
        viewModelCoroutineScope.launch {
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.value = ScreenState.Working(searchResponse)
            } else {
                _liveData.value = ScreenState.Error(
                    Throwable("Search results or total count are null")
                )
            }
        }
    }
}