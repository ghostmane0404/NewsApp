package com.mydummycompany.newsapp.ui.news


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mydummycompany.newsapp.R
import com.mydummycompany.newsapp.adapters.NewsAdapter
import com.mydummycompany.newsapp.base.BaseFragment
import com.mydummycompany.newsapp.databinding.NewslistFragmentBinding
import com.mydummycompany.newsapp.services.network.Status
import com.mydummycompany.newsapp.services.network.models.Articles
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewsListFragment : BaseFragment<NewslistFragmentBinding>(R.layout.newslist_fragment),NewsAdapter.ArticleListener,NewsAdapter.ScrollListener,
    MultiplePermissionsListener {
    private var page = 1
    private val newsViewModel: NewsViewModel by sharedViewModel()
   // private var viewManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    var viewAdapter = NewsAdapter(onArticleListener = this,scrollListener = this)


    override fun init() {
        if(isConnected()){
            setData()
        }
        else{
            setDataDb()
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.progressBar.visibility = View.VISIBLE
            if(isConnected()){
                setData()
            }
            else{
                setDataDb()
            }
        }
    }


    private fun isConnected():Boolean{
        //checking internet access
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    //initialize fragment with data from  db
    private fun setDataDb(){
        binding.commonProgress.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        newsViewModel.getNewsFromDb().observe(viewLifecycleOwner, Observer {
            binding.commonProgress.visibility = View.GONE
            if(it == "fetch succeed"){
                if(newsViewModel.newsList.isNotEmpty()){
                    viewAdapter.set(newsViewModel.newsList)
                    binding.newsRecycler.layoutManager = LinearLayoutManager(context)
                    binding.newsRecycler.adapter = viewAdapter
                    binding.newsRecycler.hasFixedSize()
                    binding.swipeRefresh.isRefreshing = false
                }
                else{
                    Toast.makeText(requireContext(),"Нет локально сохраненных новостей",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //initialize fragment with data from  server
    private fun setData(){
        binding.newsRecycler.visibility = View.GONE
        binding.commonProgress.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        page = 1
        newsViewModel.getNewsList(page = page,pageSize = 10,query = "bitcoin").observe(viewLifecycleOwner, Observer{
            binding.newsRecycler.visibility = View.VISIBLE
            binding.commonProgress.visibility = View.GONE
            when(it.status){
                Status.SUCCESS -> {
                    viewAdapter.set(it.data!!.articles)
                    newsViewModel.newsList.addAll(it.data!!.articles)
                    binding.newsRecycler.layoutManager = LinearLayoutManager(context)
                    binding.newsRecycler.adapter = viewAdapter
                    binding.newsRecycler.hasFixedSize()
                }
                Status.ERROR ->{
                    Log.d("ERROR",it.msg!!)
                }
            }
            binding.swipeRefresh.isRefreshing = false
        })
    }

    //open detailed fragment
    override fun onItemClick(position: Int) {
        val bundle  = Bundle()
        bundle.putInt("element",position)
        findNavController().navigate(R.id.detailNewsFragment,bundle)
    }

    //handle reaching end of recycler view's data and initiate lazy loading
    override fun onReachLastElement() {
        page++
        binding.progressBar.visibility = View.VISIBLE
        newsViewModel.getNewsList(page = page,pageSize = 10,query = "bitcoin").observe(viewLifecycleOwner, Observer{
            binding.progressBar.visibility = View.GONE
            when(it.status){
                Status.SUCCESS -> {
                    viewAdapter.add(it.data!!.articles )
                    newsViewModel.newsList.addAll(it.data!!.articles)
                }
                Status.ERROR ->{
                    Log.d("ERROR",it.msg!!)
                    Toast.makeText(requireContext(),"Конец списка новостей",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

   fun saveToCache(){
       Dexter.withActivity(requireActivity())
           .withPermissions(
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
               android.Manifest.permission.READ_EXTERNAL_STORAGE
           ).withListener(this)
           .onSameThread()
           .check()
   }


    //asking for runtime permissions to cache data
    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        if (report!!.areAllPermissionsGranted()){
            newsViewModel.saveCache().observe(viewLifecycleOwner, Observer {
                Log.d("STATUS",it)
            })
        }
        if (!report.areAllPermissionsGranted()) {
            Toast.makeText(requireContext(), "Для кэширования новостей нужны разрешения", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        token!!.continuePermissionRequest();
    }


}