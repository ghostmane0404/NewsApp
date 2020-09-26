package com.mydummycompany.newsapp.services.network

import com.mydummycompany.newsapp.services.network.responses.TotalResultsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getNewsList(@Query("pageSize") pageSize:Int, @Query("page")page:Int,@Query("q")query:String):Response<TotalResultsResponse>

}