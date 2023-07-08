package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.scheduler.SchedulerProviderStub
import com.geekbrains.tests.view.search.ViewSearchContract
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SearchPresenterTestRx {

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(repository, SchedulerProviderStub())
        presenter.viewContract = viewContract
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }

    @Test
    fun searchGitHub_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)

    }

    @Test
    fun handleResponseError_TotalCountIsNull() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(null, listOf()))
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract,times(1))
            .displayError("Search results or total count are null")
    }

    @Test
    fun handleResponseError_TotalCountIsNUll_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(null, listOf()))
        )

        presenter.searchGitHub(SEARCH_QUERY)

        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError("Search results or total count are null")
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test
    fun handleResponseSuccess() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(42, listOf()))
        )

        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displaySearchResults(listOf(),42)
    }
}