package com.mydummycompany.newsapp.services.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Source", foreignKeys = [
    androidx.room.ForeignKey(
        entity = ArticleEntity::class,
        parentColumns = kotlin.arrayOf("id"),
        childColumns = kotlin.arrayOf("articleId"),
        onDelete = androidx.room.ForeignKey.CASCADE
    )
])
data class SourceEntity(
    @PrimaryKey
    var innerId:String,
    @ColumnInfo
    var id: String = "",
    @ColumnInfo
    var name: String = "",
    @ColumnInfo
    var articleId:String
)