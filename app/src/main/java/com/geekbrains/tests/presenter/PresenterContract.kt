package com.geekbrains.tests.presenter

interface PresenterContract<V> {
    fun onAttach(view: V)
    fun onDetach()
}
