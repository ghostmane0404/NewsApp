package com.mydummycompany.newsapp.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mydummycompany.newsapp.R
import com.mydummycompany.newsapp.base.GenericRecyclerAdapter
import com.mydummycompany.newsapp.base.ViewHolder
import com.mydummycompany.newsapp.services.network.models.Articles
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(val onArticleListener: ArticleListener, list:ArrayList<Articles> = ArrayList(),val scrollListener: ScrollListener):
    GenericRecyclerAdapter<Articles>(list){


    interface ArticleListener{
        fun onItemClick(position: Int)
    }
    interface ScrollListener {
        fun onReachLastElement()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.news_item)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bind(items[position], holder as ViewHolder,position)
        if(position==itemCount-1  && scrollListener !=null){
            scrollListener!!.onReachLastElement()
        }
    }
    override fun bind(item: Articles, holder: ViewHolder,position: Int) {
        holder.itemView.setOnClickListener {
            onArticleListener.onItemClick(position)
        }
        holder.itemView.title.setText(item.title)
        holder.itemView.subtitle.setText(item.description)
        Glide.with(holder.itemView.article_image.context).load(item.urlToImage).into(holder.itemView.article_image)
    }


}