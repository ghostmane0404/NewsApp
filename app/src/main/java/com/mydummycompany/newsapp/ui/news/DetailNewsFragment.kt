package com.mydummycompany.newsapp.ui.news

import com.bumptech.glide.Glide
import com.mydummycompany.newsapp.R
import com.mydummycompany.newsapp.base.BaseFragment
import com.mydummycompany.newsapp.databinding.FragmentDetailNewsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class DetailNewsFragment : BaseFragment<FragmentDetailNewsBinding>(R.layout.fragment_detail_news) {
    private val newsViewModel: NewsViewModel by sharedViewModel()
    override fun init() {
        var position = arguments?.getInt("element")

        binding.viewModel = newsViewModel.newsList[position!!]
        Glide.with(requireContext()).load(newsViewModel.newsList[position!!].urlToImage)
            .into(binding.newsImage)
    }
}