package com.example.manualdi.data

import com.example.manualdi.domain.AuthRepository


class AuthRepositoryImpl(
    private val api: AuthApi
): AuthRepository {

    override suspend fun login(email: String, password: String) {
        try {
            println("Logging in...")
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}