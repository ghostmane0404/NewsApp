package com.mydummycompany.newsapp.services.network.models


data class Articles(
    var source: Source,
    var author:String?,
    var title:String?,
    var description:String?,
    var url:String?,
    var urlToImage: String?,
    var publishedAt: String?,
    var content:String?,

)