package com.mydummycompany.newsapp

import android.app.Application
import android.content.Context
import com.mydummycompany.newsapp.injections.viewModelModule
import com.mydummycompany.newsapp.utils.PreferencesUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(getModule())
        }
        PreferencesUtil.init(this)
    }

    private fun getModule(): List<Module> {
        return listOf(viewModelModule)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}