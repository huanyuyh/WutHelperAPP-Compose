package com.huanyu.hyc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.huanyu.hyc.Utils.SharedPreferenceUtil
import kotlinx.coroutines.launch

class ConfigViewModel: AndroidViewModel(application = Application()) {
    val sharedPreferences = Application().getSharedPreferences(SharedPreferenceUtil.CONFIG_FILE,Application.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString(key, value).apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        viewModelScope.launch {
            sharedPreferences.edit().putInt(key, value).apply()
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean(key, value).apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun remove(key: String) {
        viewModelScope.launch {
            sharedPreferences.edit().remove(key).apply()
        }
    }
}