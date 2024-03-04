package com.huanyu.connect.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huanyu.hyc.viewmodel.WebPageIntent
import com.huanyu.hyc.viewmodel.WebviewViewModel
import kotlinx.coroutines.launch

@Composable
fun PopWindowDemo(isMenuOpen: Boolean, onMenuToggle: (Boolean) -> Unit, modifier: Modifier,url: String,context: Context) {

    val options = listOf("在浏览器打开", "Option 2", "Option 3")
    val selectedIndex = remember { mutableStateOf(0) }

//    Column {
//        Button(
//            onClick = { onMenuToggle(!isMenuOpen) },
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = "Open PopWindow")
//        }
//
//        DropdownMenu(
//            expanded = isMenuOpen,
//            onDismissRequest = { onMenuToggle(false) }
//        ) {
//            options.forEachIndexed { index, option ->
//                DropdownMenuItem(text = { Text(text = option) },onClick = {
//                    selectedIndex.value = index
//                    onMenuToggle(false)
//                })
//            }
//        }
//
//        Text(
//            text = "Selected Option: ${options[selectedIndex.value]}",
//            modifier = Modifier.padding(16.dp)
//        )
//    }
    Column (modifier = modifier){
        DropdownMenu(
            modifier = modifier,
            expanded = isMenuOpen,
            onDismissRequest = { onMenuToggle(false) }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(text = { Text(text = option) },onClick = {
                    selectedIndex.value = index
                    when(index){
                        0->{
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }
                    }
                    onMenuToggle(false)
                })
            }
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CustomWebView(
    modifier: Modifier = Modifier,
    url: String,
    onBack: (webView: WebView?) -> Unit,
    onProgressChange: (progress: Int) -> Unit = {},
    onPageFinished: (view: WebView?, url: String?) -> Unit = { webView: WebView?, url: String? -> },
    onConsoleMessage: (consoleMessage: ConsoleMessage?) -> Unit = {},
    initSettings: (webSettings: WebSettings?) -> Unit = {},
    onReceivedError: (error: WebResourceError?) -> Unit = {},
    mainNavHost: NavHostController,
){
    var urlText by remember {
        mutableStateOf(url)
    }
    var newUrl by remember {
        mutableStateOf(url)
    }

    val webViewChromeClient = object: WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            //回调网页内容加载进度
            onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            onConsoleMessage(consoleMessage)
            return super.onConsoleMessage(consoleMessage)
        }
    }
    val webViewClient = object: WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?,
                                   favicon: Bitmap?) {

            view?.evaluateJavascript("var temp = alert;\n" +
                    "alert=null;\n" +
                    "alert(1);\n" +
                    "alert=temp; ", ValueCallback {

            } )
            super.onPageStarted(view, url, favicon)
            onProgressChange(-1)
        }
        override fun onPageFinished(view: WebView?, url: String?) {

            view?.title?.let {
                urlText = it
            }
            url?.let {
                newUrl = it
            }
            super.onPageFinished(view, url)
            onPageFinished(view,url)
            onProgressChange(100)
        }
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if(null == request?.url) return false
            val showOverrideUrl = request?.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")) {
                    //处理非http和https开头的链接地址
                    Intent(Intent.ACTION_VIEW, Uri.parse(showOverrideUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        view?.context?.applicationContext?.startActivity(this)
                    }
                    return true
                }else{
                    view?.loadUrl(showOverrideUrl)
                    return true
                }
            }catch (e:Exception){
                //没有安装和找到能打开(「xxxx://openlink.cc....」、「weixin://xxxxx」等)协议的应用
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)

        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            //自行处理....
            onReceivedError(error)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed();
            super.onReceivedSslError(view, handler, error)
        }
    }
    val webviewViewModel:WebviewViewModel = viewModel()
    var webView:WebView? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    val webPageState by webviewViewModel.webPageState.collectAsState()
    webviewViewModel.webPageState.value.copy(url)
    ConstraintLayout (modifier = modifier.fillMaxSize()){
        val (web,toolbar,pop,bottombar) = createRefs()
        val topGuide = createGuidelineFromTop(0.05f)
        val bottomGuide = createGuidelineFromBottom(0.05f)
        var isMenuOpen by remember { mutableStateOf(false) }

        val context = LocalContext.current
        PopWindowDemo(context = context,url = url,modifier = Modifier.constrainAs(pop){
            top.linkTo(topGuide)
            end.linkTo(parent.end)
            absoluteRight.linkTo(parent.absoluteRight)
        },isMenuOpen = isMenuOpen,onMenuToggle = { isOpen -> isMenuOpen = isOpen })

        Row (modifier = Modifier
            .padding(0.dp)
            .constrainAs(toolbar) {
                top.linkTo(parent.top)
                bottom.linkTo(topGuide)
                height = Dimension.fillToConstraints
                width = Dimension.matchParent
            }){
            IconButton(modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.1f)
                ,onClick = { mainNavHost.navigate("main") },) {
                Icon(Icons.Filled.ArrowBack, null)
            }
            Row (modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(0.dp)
                .weight(0.8f)
                .wrapContentHeight()){
                BasicTextField( modifier = Modifier
                    .onFocusChanged {
                        if (it.isFocused) {
                            urlText = newUrl
                        } else {
                            webView?.title?.let {
                                urlText = it
                            }
                        }
                    }
                    .align(Alignment.CenterVertically)
                    .padding(0.dp)
                    .weight(0.9f)
                    .wrapContentHeight(),singleLine = true,value = urlText
                    , onValueChange = {
                    urlText = it
                    })
                IconButton(
                    onClick = { /* 处理图标点击事件 */
                        webView?.loadUrl(urlText)
                    },
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .weight(0.1f)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Forward",
                        tint = Color.Gray
                    )
                }
            }

            IconButton(modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.1f)
                ,onClick = {
                    isMenuOpen = !isMenuOpen

                },) {
                Icon(Icons.Filled.MoreVert, null)
            }
        }
            ConstraintLayout (modifier = Modifier
//                .border(width = 2.dp, color = Color.Blue)
                .fillMaxWidth()
//            .fillMaxHeight()
                .constrainAs(web) {
                    top.linkTo(topGuide)
                    bottom.linkTo(bottomGuide)
                    height = Dimension.fillToConstraints
                    width = Dimension.matchParent
                }){
            AndroidView(
                modifier = Modifier.fillMaxSize()
//                    .border(width = 2.dp, color = Color.Red)
                ,
                factory = { ctx ->
                    WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
                        this.webViewClient = webViewClient
                        this.webChromeClient = webViewChromeClient
                        //回调webSettings供调用方设置webSettings的相关配置
                        initSettings(this.settings)
                        webView = this

                        webView?.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                            val i:Intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(ctx,i,null)
                        }
                        loadUrl(webPageState.url)
                    }
                }
            )
        }

        Row (modifier = Modifier.constrainAs(bottombar){
            top.linkTo(bottomGuide)
            bottom.linkTo(parent.bottom)
            height = Dimension.preferredWrapContent
        }){
            IconButton(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                webView?.goBack()
            }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft, contentDescription = "上一页")
            }
            IconButton(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                webView?.goForward()
            }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "下一页")
            }
            IconButton(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                webView?.loadUrl(url)
            }) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "主页")
            }
            IconButton(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                webView?.clearCache(true)
                webviewViewModel.processIntent(WebPageIntent.Reload)
            }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "刷新")
            }
            IconButton(modifier = Modifier.weight(1f),onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.List, contentDescription = "功能列表")
            }
        }

    }



    BackHandler {

        coroutineScope.launch {
            //自行控制点击了返回按键之后，关闭页面还是返回上一级网页
            onBack(webView)
        }
    }
}