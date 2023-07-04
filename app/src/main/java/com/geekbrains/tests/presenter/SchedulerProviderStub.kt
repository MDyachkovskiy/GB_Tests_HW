package com.geekbrains.tests.presenter

import io.reactivex.schedulers.Schedulers

class SchedulerProviderStub : SchedulerProvider {
    override fun ui() = Schedulers.trampoline()

    override fun io() = Schedulers.trampoline()
}