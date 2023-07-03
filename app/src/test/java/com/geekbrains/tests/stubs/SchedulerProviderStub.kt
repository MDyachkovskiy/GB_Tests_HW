package com.geekbrains.tests.stubs

import com.geekbrains.tests.presenter.SchedulerProvider
import io.reactivex.schedulers.Schedulers

class SchedulerProviderStub : SchedulerProvider {
    override fun ui() = Schedulers.trampoline()

    override fun io() = Schedulers.trampoline()
}