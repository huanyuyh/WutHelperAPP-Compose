package com.huanyu.hyc.ui.customcompose

import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebSettings


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.huanyu.connect.ui.main.CustomWebView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWebShow(modifier: Modifier, url: String, js: String, mainNavHost: NavHostController){

    ConstraintLayout (modifier = modifier.fillMaxSize()){
        val (progressBar,web) = createRefs()
        var rememberWebProgress: Int by remember { mutableStateOf(-1) }
//
//                IconButton(modifier = Modifier.constrainAs(more){
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                    absoluteLeft.linkTo(rightGuide)
//                    absoluteRight.linkTo(parent.absoluteRight)
//                    width = Dimension.fillToConstraints
//                    height = Dimension.fillToConstraints
//                },onClick = { /*TODO*/ }) {
//                    Icon(Icons.Filled.MoreVert,null)
//                }
        CustomWebView(
            modifier = Modifier
            .constrainAs(web) {
                top.linkTo(progressBar.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }, url = url,
            onBack = { webView ->
                //可根据需求处理此处
                if (webView?.canGoBack() == true) {
                    //返回上一级页面
                    webView.goBack()
                } else {
                    //关闭activity
//                finish()
                    mainNavHost.navigate("main")
                }
            },
            onProgressChange = {progress ->
                rememberWebProgress = progress
            },
            onPageFinished = {view, url ->
                view?.evaluateJavascript(js, ValueCallback {

                })
            },
            initSettings = {settings->
                settings?.apply {
                    //支持js交互
                    javaScriptEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls =false
                    cacheMode = WebSettings.LOAD_DEFAULT
                    allowFileAccess = true
                    javaScriptCanOpenWindowsAutomatically = true
                    defaultTextEncodingName = "utf-8"
                    userAgentString = "Mozilla/5.0 (Linux; Android 7.0; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/10.3 SearchCraft/2.6.2 (Baidu; P1 7.0)"
                    setDomStorageEnabled(true)
                    domStorageEnabled = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//                    setRenderPriority(WebSettings.RenderPriority.HIGH);
//                    setEnableSmoothTransition(true);
                    //....
                }
            },
            onReceivedError = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(ContentValues.TAG,">>>>>>${it?.description}")
                }
            },
            mainNavHost = mainNavHost
        )

        LinearProgressIndicator(
            progress = rememberWebProgress * 1.0F / 100F,
            color = Color.Blue,
            modifier = Modifier
                .constrainAs(progressBar) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
                .height(if (rememberWebProgress == 100) 0.dp else 3.dp)
        )

    }



}

