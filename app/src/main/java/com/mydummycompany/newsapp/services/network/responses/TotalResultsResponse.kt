package com.mydummycompany.newsapp.services.network.responses

import com.mydummycompany.newsapp.services.network.models.Articles

data class TotalResultsResponse(
    var status:String?,
    var totalResults:Int?,
    var articles: ArrayList<Articles>
)