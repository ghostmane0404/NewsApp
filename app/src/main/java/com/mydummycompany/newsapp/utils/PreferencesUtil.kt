package com.mydummycompany.newsapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtil() {


    companion object {
        private lateinit var preferences: SharedPreferences
        private const val PREFERENCE_NAME = "com.dummycompany.APP_PREF"
        fun init(context: Context) {
            preferences = context.getSharedPreferences(Companion.PREFERENCE_NAME, Context.MODE_PRIVATE)
            preferences.edit().putString("TOKEN", "c724be4f4b1f4f52bdc34a8547291bf3").apply()
            preferences.edit().apply()
        }
        fun getToken(): String? {
            return preferences.getString("TOKEN", "")
        }
    }
}