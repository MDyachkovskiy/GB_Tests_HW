package com.geekbrains.tests.di

import com.geekbrains.tests.scheduler.SearchSchedulerProvider
import com.geekbrains.tests.repository.NetworkModule
import com.geekbrains.tests.repository.RepositoryModule
import com.geekbrains.tests.view.details.DetailsViewModel
import com.geekbrains.tests.view.search.SearchViewModel
import org.koin.dsl.module


val application = module {
    single { NetworkModule.createRetrofit() }
    single { RepositoryModule.createRepository(get()) }
    single { SearchSchedulerProvider() }
    factory { SearchViewModel() }
    factory { DetailsViewModel() }
}