package com.example.manualdi

import android.app.Application
import com.example.manualdi.di.AppModule
import com.example.manualdi.di.AppModuleImpl

class ManualDIApp: Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}