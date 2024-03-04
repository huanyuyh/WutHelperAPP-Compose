package com.huanyu.hyc.ui.customcompose

import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebSettings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huanyu.connect.ui.main.CustomWebView
import com.huanyu.hyc.MyApplication
import com.huanyu.hyc.Utils.FileUtil
import com.huanyu.hyc.Utils.WutTools
import com.huanyu.hyc.viewmodel.CourseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun JWCWebPage(mainNavController:NavHostController) {
    var rememberWebProgress: Int by remember { mutableStateOf(-1) }
    //用Box包裹一下CustomWebView和LinearProgressIndicator
    val scope = CoroutineScope(Dispatchers.IO)
    val courseViewModel: CourseViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
                return CourseViewModel(MyApplication.context.applicationContext as Application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    })
//    val course = Course("test",1,"tset","tset",1,2,
//        1,2,0,1f,"tset","tset","tset")
//        myAppState.viewModel.insertCourse(course)
//这样打开网页顶部就可以显示加载的进度了
    Box {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (progressBar,btnImport,btnBack) = createRefs()
            val context = LocalContext.current
            val bottomGuide = createGuidelineFromBottom(0.1f)
            CustomWebView(
                modifier = Modifier.fillMaxSize(),
                url = "http://sso.jwc.whut.edu.cn/Certification/index2.jsp",
                onPageFinished = { view, url ->

                    val downloadjs = "var context = '<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>';\n" +
                            "console.log(context);"
                    view?.evaluateJavascript(downloadjs, ValueCallback {
                    })
                },
                onConsoleMessage = {consoleMessage ->
                    consoleMessage?.let {
                        Log.d("jwcConsole", consoleMessage.message())
                        FileUtil.saveText(path = context.externalCacheDir!!.path+"/jwc.html", text = consoleMessage.message()?:"")
                    }

                },
                onProgressChange = {progress ->
                    rememberWebProgress = progress
                },
                initSettings = {settings->
                    settings?.apply {
                        //支持js交互
                        javaScriptEnabled = true
                        defaultTextEncodingName = "utf-8"
                        cacheMode = WebSettings.LOAD_NO_CACHE
                        domStorageEnabled = true
                        //....
                    }
                }, onBack = { webView ->
                    //可根据需求处理此处
                    if (webView?.canGoBack() == true) {
                        //返回上一级页面
                        webView.goBack()
                    } else {
                        //关闭activity
//                finish()
                    }
                },onReceivedError = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d(ContentValues.TAG,">>>>>>${it?.description}")
                    }
                }, mainNavHost = mainNavController)

            LinearProgressIndicator(
                progress = rememberWebProgress * 1.0F / 100F,
                color = Color.Blue,
                modifier = Modifier
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .height(if (rememberWebProgress == 100) 0.dp else 3.dp))
//            Button(modifier = Modifier.constrainAs(btnBack){
//                top.linkTo(parent.top)
//            },onClick = { mainNavController.apply {
//
//             }.navigate("main"){
//                popUpTo("jwc") {
//                    inclusive = true
//                }
//            } }) {
//                Text(text = "返回")
//            }
//            Button(modifier = Modifier.constrainAs(btnImport){
//                absoluteRight.linkTo(parent.absoluteRight)
//            },onClick = {
//                var courselist = WutTools.readCourses(context)
//                courselist?.let {
////                    myAppState.viewModel.clearCourse()
//                    courseViewModel.clearCourse()
//                    courselist.forEach {
//                        Log.d("course", it.toString())
//
//                        courseViewModel.insertCourse(it)
//                    }
//                }
//            }) {
//                Text(text = "导入")
//            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .constrainAs(btnImport) {
                        bottom.linkTo(bottomGuide)
                        end.linkTo(parent.end)
                    },
                icon = { Icon(Icons.Filled.Add, modifier = Modifier.size(20.dp), contentDescription = null) },
                text = { Text("导入") },
                onClick = { /*do something*/
                    var courselist = WutTools.readCourses(context)
                    courselist?.let {
//                    myAppState.viewModel.clearCourse()
                        courseViewModel.clearCourse()
                        courselist.forEach {
                            Log.d("course", it.toString())

                            courseViewModel.insertCourse(it)
                        }
                    }
                }
            )
        }

    }



}