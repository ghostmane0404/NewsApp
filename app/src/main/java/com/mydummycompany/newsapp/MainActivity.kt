package com.mydummycompany.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.mydummycompany.newsapp.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:MainActivityBinding
    private var pressCount  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.main_activity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        val navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment
        val fragment = navHostFragment!!.childFragmentManager.fragments[0]
        if(fragment is com.mydummycompany.newsapp.ui.news.NewsListFragment){
            pressCount++
            if(pressCount>=2){
                pressCount = 0

                super.onBackPressed()
            }
            if(pressCount==1){
                fragment.saveToCache()
                Toast.makeText(this,"Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show()
            }
        }
        if(fragment is com.mydummycompany.newsapp.ui.news.DetailNewsFragment){
            super.onBackPressed()
        }
    }


}