package com.mydummycompany.newsapp.services.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Article")
data class ArticleEntity(
    @PrimaryKey
    var id:String,
    var author:String = "",
    var title:String = "",
    var description:String = "",
    var url:String = "",
    var urlToImage: String = "",
    var publishedAt: String = "",
    var content:String = "",
    )