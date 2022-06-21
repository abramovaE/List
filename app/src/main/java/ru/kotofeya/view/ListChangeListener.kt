package ru.kotofeya.view

interface ListChangeListener {
    fun onTextDelete(text: String)
    fun itemMove(fromPosition: Int, toPosition: Int)
}