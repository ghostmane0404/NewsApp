package com.mydummycompany.newsapp.injections

import com.mydummycompany.newsapp.ui.news.NewsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { NewsViewModel(androidApplication()) }
}