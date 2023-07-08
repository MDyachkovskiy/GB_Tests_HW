package com.geekbrains.tests.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel : ViewModel() {

    val countLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    fun setCounter(count: Int) {
        countLiveData.value = count
    }

    fun onIncrement() {
        countLiveData.value = (countLiveData.value ?: 0) + 1
    }

    fun onDecrement() {
        countLiveData.value = (countLiveData.value ?: 0) - 1
    }
}