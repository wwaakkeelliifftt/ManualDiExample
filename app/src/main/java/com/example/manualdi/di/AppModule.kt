package com.example.manualdi.di

import android.content.Context
import com.example.manualdi.appsearch.TodoSearchManager
import com.example.manualdi.data.AuthApi
import com.example.manualdi.data.AuthRepositoryImpl
import com.example.manualdi.domain.AuthRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppModule {
    val authApi: AuthApi
    val authRepository: AuthRepository
    val todoSearchManager: TodoSearchManager
}

class AppModuleImpl(
    val appContext: Context
): AppModule {

    override val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://something.url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApi)
    }

    override val todoSearchManager: TodoSearchManager by lazy {
        TodoSearchManager(appContext)
    }

}