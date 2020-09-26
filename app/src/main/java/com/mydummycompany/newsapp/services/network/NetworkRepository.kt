package com.mydummycompany.newsapp.services.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class NetworkRepository {
    private val error = "Ошибка при получении данных"

    fun getNewsList(pageSize: Int, page: Int,query:String) = liveData(Dispatchers.IO) {
        try {
            val response = NewsClient.apiService().getNewsList(pageSize, page,query)
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error(response.errorBody().toString()))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(error))
        }
    }
    }
