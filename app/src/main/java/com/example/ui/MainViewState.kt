package com.example.ui

sealed class MainViewState {
    object Initial : MainViewState()
    data class Success(val str: String): MainViewState()
    object Failure : MainViewState()
}