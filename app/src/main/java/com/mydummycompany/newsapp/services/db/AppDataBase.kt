package com.mydummycompany.newsapp.services.db


import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mydummycompany.newsapp.services.network.models.Articles
import com.mydummycompany.newsapp.services.network.models.Source


@Database(
    entities = [
        com.mydummycompany.newsapp.services.db.entities.SourceEntity::class,
        com.mydummycompany.newsapp.services.db.entities.ArticleEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDao(): RoomDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun instance(context: Application): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "NewsApp"
                ).build()
                this.instance = instance
                instance
            }

        }
    }
}
