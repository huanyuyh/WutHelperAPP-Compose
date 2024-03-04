package com.huanyu.hyc.ui.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.huanyu.hyc.MyApplication
import com.huanyu.hyc.ui.table.TablePage
import com.huanyu.hyc.viewmodel.UsersViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainView(modifier: Modifier, mainNavHost: NavHostController)
{
    val usersViewModel: UsersViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                return UsersViewModel(MyApplication.context.applicationContext as Application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    })
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (pager,bottombar) = createRefs()
        val bottomGuide = createGuidelineFromBottom(0.06f)
        val pagerState = rememberPagerState(initialPage = 1)

        HorizontalPager(modifier= Modifier.constrainAs(pager){
            top.linkTo(parent.top)
            bottom.linkTo(bottomGuide)
            height = Dimension.fillToConstraints
        },pageCount = 3, state = pagerState, beyondBoundsPageCount =2) {
            when(it){
                0 -> TablePage(mainNavHost = mainNavHost)
                1 -> HomePage(modifier = modifier,mainNavHost = mainNavHost)
                2 -> ServiceWebList(usersViewModel = usersViewModel,modifier = modifier, mainNavHost = mainNavHost)
            }

        }
        val navController = rememberNavController()
        BottomNaviBar(modifier = Modifier.constrainAs(bottombar){
            top.linkTo(bottomGuide)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        },navController = navController, pagerState = pagerState)
        NavHostContainer(navController = navController, pagerState = pagerState)
    }
}



object WebData{
    val webMap:Map<String,String> = mutableMapOf(
        "智慧理工大" to "http://zhlgd.whut.edu.cn/",
        "教务系统（智慧理工大）" to "http://sso.jwc.whut.edu.cn/Certification/index2.jsp",
        "教务系统" to "http://sso.jwc.whut.edu.cn/Certification/toIndex.do",
        "缴费平台" to "cwsf.whut.edu.cn",
        "智慧学工" to "https://talent.whut.edu.cn/",
        "校园地图（智慧理工大版）" to "http://gis.whut.edu.cn/index.shtml",
        "校园地图（微校园版）" to "http://gis.whut.edu.cn/mobile/index.html#/",
        "WebVPN" to "https://webvpn.whut.edu.cn/",
        "学校邮箱" to "https://mail.whut.edu.cn/",
        "校园网认证(自动)" to "http://1.1.1.1",
        "校园网认证" to "http://172.30.21.100/",
        "成绩查询" to "http://zhlgd.whut.edu.cn/tp_up/view?m=up#act=up/sysintegration/queryGrade",
        "讯飞星火" to "https://xinghuo.xfyun.cn/desk"
    )
    val webJsMap:Map<String,String> = mutableMapOf(
        "智慧理工大" to "document.getElementById('un').value = '0122109361613'\n" +
                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                "document.getElementById('index_login_btn').click()\n",
        "教务系统（智慧理工大）" to "document.getElementById('un').value = '0122109361613'\n" +
                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                "document.getElementById('index_login_btn').click()\n",
        "教务系统" to "javascript:document.getElementById('username').value ='0122109361613'\n" +
                "document.getElementById('password').value='NYH3!14nyh'\n",
        "缴费平台" to "cwsf.whut.edu.cn",
        "智慧学工" to "https://talent.whut.edu.cn/",
        "校园地图（智慧理工大版）" to "http://gis.whut.edu.cn/index.shtml",
        "校园地图（微校园版）" to "http://gis.whut.edu.cn/mobile/index.html#/",
        "WebVPN" to "https://webvpn.whut.edu.cn/",
        "学校邮箱" to "https://mail.whut.edu.cn/",
        "校园网认证(自动)" to "http://1.1.1.1",
        "校园网认证" to "http://172.30.21.100/",
        "成绩查询" to "http://zhlgd.whut.edu.cn/tp_up/view?m=up#act=up/sysintegration/queryGrade",
        "讯飞星火" to ""
    )
    val webList:List<String> = mutableListOf(
        "智慧理工大",
        "教务系统（智慧理工大）",
        "教务系统",
        "缴费平台",
        "智慧学工",
        "校园地图（智慧理工大版）",
        "校园地图（微校园版）",
        "WebVPN",
        "学校邮箱",
        "校园网认证(自动)",
        "校园网认证WLAN",
        "成绩查询",
        "讯飞星火"
    )
}

@Composable
fun WebPageButton(
    modifier: Modifier,
    mainNavHost: NavHostController,
    name: String,
    viewModel: UsersViewModel
) {

    Card(
        modifier = modifier
            .padding(5.dp)
            .clickable{
//                viewModel.searchUsers(name)
                mainNavHost.navigate("web/${name}")
            }

    ) {
        Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = name, fontSize = 25.sp)
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ServiceWebList(
    modifier: Modifier,
    mainNavHost: NavHostController,
    usersViewModel: UsersViewModel,

    ){

//    val usersDao = UsersDatabase.getDatabase(context.applicationContext as Application).usersDao()


    val userList by usersViewModel.getUsers().observeAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    ConstraintLayout (modifier = modifier.fillMaxSize()){
        Column (modifier = modifier){
            userList?.let {
                for (i in 0 until userList!!.size step 2){
                    Row (modifier = Modifier.height((screenHeight/10).dp)){

                        WebPageButton(viewModel = usersViewModel,
                            modifier = modifier.weight(weight = 0.5f),
                            mainNavHost = mainNavHost ,
                            name = userList!![i].platform)
                        if(i+1<userList!!.size)
                            WebPageButton(
                                modifier = modifier.weight(weight = 0.5f),
                                mainNavHost = mainNavHost,
                                name = userList!![i+1].platform,
                                viewModel = usersViewModel
                            )
                    }
                }
            }



        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier, mainNavHost: NavHostController){
    val scrollState = rememberScrollState()
    ConstraintLayout (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state = scrollState, enabled = true)){
        val topGuide = createGuidelineFromTop(0.2f)
        val (wifiConnect) = createRefs()
        Card (modifier = Modifier
            .fillMaxWidth()
            .constrainAs(wifiConnect) {
                top.linkTo(parent.top)
                bottom.linkTo(topGuide)
                height = Dimension.fillToConstraints
            }){
            var wifiUserName by remember {
                mutableStateOf("")
            }
            var wifiPassWord by remember {
                mutableStateOf("")
            }
            TextField(modifier = Modifier.fillMaxWidth(),value = wifiUserName, onValueChange = {wifiUserName = it}, label = { Text(text = "校园网用户名") })
            TextField(modifier = Modifier.fillMaxWidth(),value = wifiUserName, onValueChange = {wifiPassWord = it}, label = { Text(text = "校园网密码") })
            Row {
                Button(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                    mainNavHost.navigate("web/校园网认证(1.1.1.1)")}) {
                    Text(text = "校园网认证(1.1.1.1)")

                }
                Button(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                    mainNavHost.navigate("web/校园网认证(WLAN)")}) {
                    Text(text = "校园网认证(WLAN)")

                }
                Button(modifier = Modifier.weight(1f),onClick = { /*TODO*/
                    mainNavHost.navigate("web/校园网认证(isp)")}) {
                    Text(text = "校园网认证(isp)")

                }
            }

        }
    }
}