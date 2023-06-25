package com.geekbrains.tests.application

import android.app.Application
import com.geekbrains.tests.di.application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(application)
        }
    }

}