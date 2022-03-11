package ru.kotofeya

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {
    val dataSet: MutableLiveData<Set<String>> by lazy{
        MutableLiveData<Set<String>>()
    }
}