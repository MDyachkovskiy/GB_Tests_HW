package com.geekbrains.tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.model.ScreenState
import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.SchedulerProviderStub
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.search.SearchViewModel
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: GitHubRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, SchedulerProviderStub())
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "Search results or total count are null"
        private const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }

    @Test
    fun search_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )

        searchViewModel.searchGitHub((SEARCH_QUERY))
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)

    }

    @Test
    fun coroutines_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(1, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                Assert.assertNotNull(liveData.value)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(error)
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            val value: ScreenState.Error = liveData.value as ScreenState.Error
            Assert.assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_TestReturnValueIsWorking() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        val mockedSearchResponse = SearchResponse(1, listOf())

       `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(mockedSearchResponse)
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            val value: ScreenState.Working = liveData.value as ScreenState.Working
            Assert.assertEquals(value.searchResponse, mockedSearchResponse)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_TestReturnValueIsError_NullData() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(null, null))
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)

            val value: ScreenState.Error = liveData.value as ScreenState.Error
            Assert.assertEquals(value.error.message, ERROR_TEXT)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState>{}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, EXCEPTION_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }
}