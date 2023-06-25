package com.geekbrains.tests

import android.app.Application
import org.koin.core.module.Module

class TestApp : Application() {
    lateinit var injector: Module
}