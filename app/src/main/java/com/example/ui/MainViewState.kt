package com.example.ui

sealed class MainViewState {
    object Initial : MainViewState()
    data class HasData(val str: String): MainViewState()
}