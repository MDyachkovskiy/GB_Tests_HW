package com.geekbrains.tests.di

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import com.geekbrains.tests.presenter.search.PresenterSearchContract
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.NetworkModule
import com.geekbrains.tests.repository.RepositoryModule
import org.koin.dsl.module


val application = module {
    single { NetworkModule.createRetrofit() }
    single { RepositoryModule.createRepository(get()) }
    single<PresenterSearchContract> { SearchPresenter(get()) }
    single<PresenterDetailsContract> {DetailsPresenter()}
}