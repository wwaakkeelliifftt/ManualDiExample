package com.example.manualdi.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manualdi.domain.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    val response = MutableStateFlow("...")
    val timer = MutableStateFlow("00:00")

    fun login() {
        viewModelScope.launch {
            authRepository.login("test@test.com", "test123")
            timerActivate()
        }
    }

    private fun timerActivate() {
        viewModelScope.launch {
            for (i in 5 downTo 1) {
                timer.emit("00:0$i")
                delay(1000)
            }
            timer.emit("00:00")
            delay(200)
            response.emit("Done! Got it")
        }

    }
}