package com.example.manualdi.domain

interface AuthRepository {
    suspend fun login(email: String, password: String)
}