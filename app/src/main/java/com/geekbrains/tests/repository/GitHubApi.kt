package com.geekbrains.tests.repository

import com.geekbrains.tests.model.SearchResponse
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Документация https://developer.github.com/v3/search/
 */

internal interface GitHubApi {

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/repositories")
    fun searchGithub(@Query("q") term: String?): Call<SearchResponse?>?

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/repositories")
    fun searchGithubRx(
        @Query("q") term: String?
    ) : Observable<SearchResponse>

    @Headers("Accept: application/vnd.github.mercy_preview+json")
    @GET("search/repositories")
    suspend fun searchGithubAsync(
        @Query("q") term: String?
    ) : Deferred<SearchResponse>

}
