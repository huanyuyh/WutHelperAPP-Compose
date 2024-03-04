package com.huanyu.hyc

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huanyu.hyc.ui.customcompose.JWCWebPage
import com.huanyu.hyc.ui.customcompose.MyWebShow
import com.huanyu.hyc.ui.main.MainView
import com.huanyu.hyc.ui.theme.HYCTheme
import com.huanyu.hyc.viewmodel.UsersViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainNavHost:NavHostController = rememberNavController()

            HYCTheme {
                var modifier:Modifier = Modifier
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val usersViewModel: UsersViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                                return UsersViewModel(MyApplication.context.applicationContext as Application) as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }

                    })
                    val userList by usersViewModel.getUsers().observeAsState()
                    NavHost(navController = mainNavHost, startDestination = "main") {
                        composable("main") {
                            MainView(modifier = modifier.fillMaxSize(),mainNavHost = mainNavHost) }
                        composable("course") { JWCWebPage(mainNavController = mainNavHost) }
                        composable("web/{name}", arguments = listOf(
                            navArgument("name") {
                                type = NavType.StringType   //参数类型
                                defaultValue = "智慧理工大"        //默认值
                                nullable = true          //是否可空
                            },
                            )
                        ) {
                                backStackEntry ->

                            var name = backStackEntry.arguments?.getString("name").toString()
                            var url:String? = null
                            var js:String? = null
                            userList?.forEach {
                                if(it.platform.equals(name)){
                                    url = it.webUrl
                                    js = it.webJS
                                }
                            }
                            Log.d("base",usersViewModel.searchUsers(name).toString())
//                            var url = usersViewModel.user?.value?.webUrl
//                            var js = usersViewModel.user?.value?.webUrl


                            MyWebShow(modifier = modifier.fillMaxSize(),url = url?:"http://zhlgd.whut.edu.cn" ,js = js?:"",mainNavHost = mainNavHost)
                        }
                        /*...*/
                    }
//                    MyWebShow(modifier = Modifier,url = "http://zhlgd.whut.edu.cn/",js =
//                            "document.getElementById('un').value = '0122109361613'\n" +
//                            "document.getElementById('pd').value = 'nyh314nyh'\n" +
//                            "document.getElementById('index_login_btn').click()\n")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HYCTheme {
        Greeting("Android")
    }
}