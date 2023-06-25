package com.geekbrains.tests.repository

import com.geekbrains.tests.model.SearchResponse
import retrofit2.Response

internal class GitHubRepository(
    private val gitHubApi: GitHubApi
    ) : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        callback.handleGitHubResponse(Response.success(SearchResponse(42, listOf())))
    }

    interface GitHubRepositoryCallback {
        fun handleGitHubResponse(response: Response<SearchResponse?>?)
        fun handleGitHubError()
    }
}