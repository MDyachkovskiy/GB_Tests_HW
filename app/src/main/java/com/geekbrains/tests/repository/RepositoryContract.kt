package com.geekbrains.tests.repository

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )
}