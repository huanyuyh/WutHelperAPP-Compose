package com.huanyu.hyc.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Model
data class WebPageState(val url: String)

// Intent
sealed class WebPageIntent {
    data class LoadUrl(val url: String) : WebPageIntent()
    object Reload : WebPageIntent()
}

class WebviewViewModel : ViewModel() {
    private val _webPageState = MutableStateFlow(WebPageState("https://www.example.com"))
    val webPageState: StateFlow<WebPageState> = _webPageState

    fun processIntent(intent: WebPageIntent) {
        when (intent) {
            is WebPageIntent.LoadUrl -> {
                _webPageState.value = WebPageState(intent.url)
            }
            WebPageIntent.Reload -> {
                // 在真实应用中可能需要进行一些额外的处理，比如清除缓存等
                _webPageState.value = _webPageState.value.copy(url = _webPageState.value.url)
            }
        }
    }
}