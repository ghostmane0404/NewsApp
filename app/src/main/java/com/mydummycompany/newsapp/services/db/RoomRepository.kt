package com.mydummycompany.newsapp.services.db

import androidx.lifecycle.LiveData
import com.mydummycompany.newsapp.services.db.entities.ArticleEntity
import com.mydummycompany.newsapp.services.db.entities.SourceEntity

class RoomRepository(private val dao: RoomDao) {
    suspend fun insertArticle(entity: ArticleEntity) {
        dao.insertArticle(entity)
    }
    suspend fun insertSource(entity: SourceEntity) {
        dao.insertSource(entity)
    }
    suspend fun getAllArticles():List<ArticleEntity>{
       return  dao.getAllArticles()
    }
    suspend fun getSource(id:String):SourceEntity{
        return dao.getSource(id)
    }

    suspend fun deleteArticles(){
        dao.deleteArticles()
    }
    suspend fun deleteSources(){
        dao.deleteSources()
    }
}