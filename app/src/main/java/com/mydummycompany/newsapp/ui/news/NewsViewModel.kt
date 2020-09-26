package com.mydummycompany.newsapp.ui.news

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bumptech.glide.Glide
import com.mydummycompany.newsapp.base.BaseViewModel
import com.mydummycompany.newsapp.services.db.entities.ArticleEntity
import com.mydummycompany.newsapp.services.db.entities.SourceEntity
import com.mydummycompany.newsapp.services.network.models.Articles
import com.mydummycompany.newsapp.services.network.Resource
import com.mydummycompany.newsapp.services.network.models.Source
import com.mydummycompany.newsapp.services.network.responses.TotalResultsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class NewsViewModel(application: Application) : BaseViewModel(application) {
    var newsList = ArrayList<Articles>()
    val context = application.applicationContext

    //get data from server
    fun getNewsList(
        pageSize: Int,
        page: Int,
        query: String
    ): LiveData<Resource<TotalResultsResponse>> {
        return network.getNewsList(pageSize, page, query)
    }

    //get data from db
    fun getNewsFromDb(): LiveData<String> = liveData {

        val job: Job = launch(Dispatchers.IO) {
            newsList.clear()
            //get all articles
            for (element in db.getAllArticles()) {
                //loop over every article and get its source by article id
                var sourceFromDb = db.getSource(element.id)
                var source = Source(id = sourceFromDb.id, name = sourceFromDb.name)

                //adding to news list to show in fragments
                newsList.add(
                    Articles(
                        source = source,
                        author = element.author,
                        title =  element.title,
                        description = element.description,
                        url = element.url,
                        urlToImage = element.urlToImage,
                        publishedAt = element.publishedAt,
                        content = element.content
                        )
                )
            }
        }
        job.join()
        if(job.isCompleted) emit("fetch succeed")
    }

    //save current data from server to database
    fun saveCache(): LiveData<String> = liveData {

        val job: Job = launch(Dispatchers.IO) {
            db.deleteArticles()
            db.deleteSources()

            val root = Environment.getExternalStorageDirectory()
            var file = File(root.absolutePath + "/NewsApp/LocalImages/")
            file.deleteRecursively()
            for (item in newsList) {
                //creating unique uuid
                var articleId = UUID.randomUUID().toString()
                //saving image to external storage
                var imagePath = saveImageFile(item.urlToImage!!)
                var articleEntity = ArticleEntity(
                    id = articleId,
                    author = item.author!!,
                    title = item.title!!,
                    description = item.description!!,
                    url = item.url!!,
                    urlToImage = imagePath,
                    publishedAt = item.publishedAt!!,
                    content = item.content!!,
                )
                //synchronously inserting article to local db
                db.insertArticle(articleEntity)
                //generating source's uuid
                var sourceId = UUID.randomUUID().toString()
                var sourceEntity = SourceEntity(
                    innerId = sourceId,
                    id = item.source.id ?: " ",
                    name = item.source.name ?: " ",
                    articleId = articleId
                )
                db.insertSource(sourceEntity)
            }
        }
        job.join()
        if (job.isCompleted) {
            emit("success")
        } else emit("error in saving")
    }


/*    //clear data from database
    fun clearCache(){
        val job:Job = launch(Dispatchers.IO) {
            db.deleteArticles()
            db.deleteSources()

            val root = Environment.getExternalStorageDirectory()
            var file = File(root.absolutePath + "/NewsApp/LocalImages/")
            file.deleteRecursively()

        }
    }*/

    fun saveImageFile(url: String): String {
        val bitmap: Bitmap = Glide.with(context).asBitmap().load(url).submit().get()
        val root = Environment.getExternalStorageDirectory()
        val dir = File(root.absolutePath + "/NewsApp/LocalImages/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        var path = Uri.parse(url).lastPathSegment
        val file = File(dir, path)
        try {
            file.writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 30)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    //writing photo to memory
    fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}