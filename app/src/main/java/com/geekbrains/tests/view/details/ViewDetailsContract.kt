package com.geekbrains.tests.view.details

import com.geekbrains.tests.view.ViewContract

interface ViewDetailsContract : ViewContract {
    fun setCount(count: Int)
}