package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.search.ViewSearchContract
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

//Тестируем наш Презентер
class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        //Обязательно для аннотаций "@Mock"
        //Раньше было @RunWith(MockitoJUnitRunner.class) в аннотации к самому классу (SearchPresenterTest)
        MockitoAnnotations.initMocks(this)
        //Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        presenter = SearchPresenter(repository)
    }

    @Test //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        val searchQuery = "some query"
        //Запускаем код, функционал которого хотим протестировать
        presenter.searchGitHub("some query")
        //Убеждаемся, что все работает как надо
        verify(repository, times(1)).searchGithub(searchQuery, presenter)
    }

    @Test
    fun onAttach_shouldAttachView() {
        try {
            presenter.onAttach(viewContract)
            presenter.searchGitHub("test")

            verify(viewContract, times(1)).displayLoading(true)
        } finally {
            presenter.onDetach()
        }
    }

    @Test
    fun onDetach_shouldDetachView() {
        presenter.onAttach(viewContract)
        presenter.onDetach()
        presenter.searchGitHub("test")

        verify(viewContract, never()).displayLoading(true)
    }

    @Test //Проверяем работу метода handleGitHubError()
    fun handleGitHubError_Test() {
        presenter.onAttach(viewContract)
        try {
            presenter.handleGitHubError()
            verify(viewContract, times(1)).displayError()
        } finally {
            presenter.onDetach()
        }
    }

    //Проверяем работу метода handleGitHubResponse

    @Test //Для начала проверим, как приходит ответ сервера
    fun handleGitHubResponse_ResponseUnsuccessful() {
        //Создаем мок ответа сервера с типом Response<SearchResponse?>?
        val response = mock(Response::class.java) as Response<SearchResponse?>
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        `when`(response.isSuccessful).thenReturn(false)
        //Убеждаемся, что ответ действительно false
        assertFalse(response.isSuccessful)
    }

    @Test //Теперь проверим, как у нас обрабатываются ошибки
    fun handleGitHubResponse_Failure() {
        presenter.onAttach(viewContract)

        try {
            val response = mock(Response::class.java) as Response<SearchResponse?>
            `when`(response.isSuccessful).thenReturn(false)
            presenter.handleGitHubResponse(response)

            verify(viewContract, times(1))
                .displayError("Response is null or unsuccessful")
        } finally {
            presenter.onDetach()
        }
    }

    @Test //Проверим порядок вызова методов viewContract
    fun handleGitHubResponse_ResponseFailure_ViewContractMethodOrder() {
        presenter.onAttach(viewContract)

        try {
            val response = mock(Response::class.java) as Response<SearchResponse?>
            `when`(response.isSuccessful).thenReturn(false)
            presenter.handleGitHubResponse(response)
            val inOrder = inOrder(viewContract)

            inOrder.verify(viewContract).displayLoading(false)
            inOrder.verify(viewContract).displayError("Response is null or unsuccessful")
        } finally {
            presenter.onDetach()
        }
    }

    @Test //Проверим пустой ответ сервера
    fun handleGitHubResponse_ResponseIsEmpty() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.body()).thenReturn(null)
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        assertNull(response.body())
    }

    @Test //Теперь проверим непустой ответ сервера
    fun handleGitHubResponse_ResponseIsNotEmpty() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.body()).thenReturn(mock(SearchResponse::class.java))
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        assertNotNull(response.body())
    }

    @Test //Проверим как обрабатывается случай, если ответ от сервера пришел пустой
    fun handleGitHubResponse_EmptyResponse() {
        presenter.onAttach(viewContract)

        try {
            val response = mock(Response::class.java) as Response<SearchResponse?>
            `when`(response.isSuccessful).thenReturn(true)
            `when`(response.body()).thenReturn(null)
            presenter.handleGitHubResponse(response)

            verify(viewContract, times(1))
                .displayError("Search results or total count are null")
        } finally {
            presenter.onDetach()
        }
    }

    @Test //Пришло время проверить успешный ответ, так как все остальные случаи мы уже покрыли тестами
    fun handleGitHubResponse_Success() {

        presenter.onAttach(viewContract)

        try {
            val response = mock(Response::class.java) as Response<SearchResponse?>
            val searchResponse = mock(SearchResponse::class.java)
            val searchResults = listOf(mock(SearchResult::class.java))

            `when`(response.isSuccessful).thenReturn(true)
            `when`(response.body()).thenReturn(searchResponse)
            `when`(searchResponse.searchResults).thenReturn(searchResults)
            `when`(searchResponse.totalCount).thenReturn(101)
            presenter.handleGitHubResponse(response)


            verify(viewContract, times(1)).displaySearchResults(searchResults, 101)
        } finally {
            presenter.onDetach()
        }
    }
}
