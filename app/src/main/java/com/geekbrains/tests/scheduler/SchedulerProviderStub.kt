package com.geekbrains.tests.scheduler

import io.reactivex.schedulers.Schedulers

class SchedulerProviderStub : SchedulerProvider {
    override fun ui() = Schedulers.trampoline()

    override fun io() = Schedulers.trampoline()
}