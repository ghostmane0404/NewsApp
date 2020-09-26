package com.mydummycompany.newsapp.services.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mydummycompany.newsapp.services.db.entities.ArticleEntity
import com.mydummycompany.newsapp.services.db.entities.SourceEntity

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(entity: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSource(entity: SourceEntity)

    @Query("Select * from Article")
    suspend fun getAllArticles():List<ArticleEntity>

    @Query("Select * from Source where articleId = :id")
    suspend fun getSource(id:String):SourceEntity


    @Query("DELETE FROM Article")
    suspend fun deleteArticles()

    @Query("DELETE FROM Source")
    suspend fun deleteSources()
}