package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.presenter.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchSchedulerProvider : SchedulerProvider {

    override fun ui() = AndroidSchedulers.mainThread()

    override fun io() = Schedulers.io()
}