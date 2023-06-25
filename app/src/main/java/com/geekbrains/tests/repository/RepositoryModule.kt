package com.geekbrains.tests.repository

import retrofit2.Retrofit

object RepositoryModule {
    fun createRepository(retrofit: Retrofit) : RepositoryContract {
        return GitHubRepository(retrofit.create(GitHubApi::class.java))
    }
}