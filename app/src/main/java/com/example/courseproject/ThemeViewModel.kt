package com.example.courseproject

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: Boolean get() = _isDarkTheme.value

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}