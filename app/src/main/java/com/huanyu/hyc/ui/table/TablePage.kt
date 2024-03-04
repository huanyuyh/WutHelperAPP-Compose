package com.huanyu.hyc.ui.table

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huanyu.hyc.MyApplication
import com.huanyu.hyc.MyApplication.Companion.context
import com.huanyu.hyc.viewmodel.CourseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TablePage(mainNavHost: NavHostController){
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val pageState:PagerState = rememberPagerState(initialPage = 9)
    val weekList = mutableListOf<String>()
    for(i in 1 until 20){
        weekList.add("第${i}周")
    }

    ConstraintLayout (modifier = Modifier.fillMaxSize()){
        val (topbar,table,floatbutton,back,forward) = createRefs()
        val coroutineScope = rememberCoroutineScope()
        var selectedOption by remember { mutableStateOf("第9周") }
        ConstraintLayout (modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight / 16).dp)
            .constrainAs(topbar) {
                top.linkTo(parent.top)
            }){
//            Button(onClick = {  }) {
//                Text(text = "导入")
//            }
            val (setList,setbutton) = createRefs()

            Spinner(items = weekList, selectedItem = selectedOption, onItemSelected = {
                selectedOption = weekList[it]
                coroutineScope.launch {
                    pageState.animateScrollToPage(it)
                }
            })
            var isMenuOpen by remember {
                mutableStateOf(false)
            }
            SettingList(context = context,modifier = Modifier.constrainAs(setList){
                top.linkTo(parent.bottom)
                end.linkTo(parent.end)
                absoluteRight.linkTo(parent.absoluteRight)
            },isMenuOpen = isMenuOpen,onMenuToggle = { isOpen -> isMenuOpen = isOpen }, onSelected = {
                when(it){
                    0->mainNavHost.navigate("course")
                }
            })
            IconButton(modifier = Modifier.constrainAs(setbutton){
                end.linkTo(parent.end)
            },onClick = { isMenuOpen = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
//            Button(onClick = { coroutineScope.launch {
//                if (week.currentPage!=0){
//                    week.animateScrollToPage(week.currentPage-1)
//                }else{
//                    week.scrollToPage(18)
//                }
//
//            } }) {
//                Text(text = "上一周")
//            }
//            Button(onClick = { coroutineScope.launch {
//                if (week.currentPage!=18){
//                    week.animateScrollToPage(week.currentPage+1)
//                }else{
//                    week.scrollToPage(0)
//                }
//
//            } }) {
//                Text(text = "下一周")
//            }
        }
        val scrollpos = rememberScrollState()
        HorizontalPager(modifier = Modifier.constrainAs(table){
            top.linkTo(topbar.bottom)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        },pageCount = 19, state = pageState, beyondBoundsPageCount = 1) {

            TableMain(modifier = Modifier.fillMaxSize(),pageState = pageState, week = it+1,scrollpos = scrollpos)
        }
        LaunchedEffect(pageState) {
            snapshotFlow { pageState.currentPage }
                .collect { currentPage ->
                    selectedOption = weekList[currentPage]
                    // 监听当前页面的变化
                    // 处理翻页事件
                    // 例如：更新其他相关的 UI 或执行某些操作
                }
        }
        if(scrollpos.value != scrollpos.maxValue) {
            FloatingActionButton(
                modifier = Modifier
                    .constrainAs(back) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(forward.start)
                    },
                onClick = { /*do something*/
                    coroutineScope.launch {
                        if (pageState.currentPage != 0) {
                            pageState.animateScrollToPage(pageState.currentPage - 1)
                        } else {
                            pageState.scrollToPage(18)
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Localized description")
            }
            FloatingActionButton(
                modifier = Modifier

                    .constrainAs(forward) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = { /*do something*/
                    coroutineScope.launch {
                        if (pageState.currentPage != 18) {
                            pageState.animateScrollToPage(pageState.currentPage + 1)
                        } else {
                            pageState.scrollToPage(0)
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Localized description")
            }

        }

//        ExtendedFloatingActionButton(
//            modifier = Modifier
//                .height(32.dp)
//                .width(96.dp)
//                .constrainAs(floatbutton) {
//                    bottom.linkTo(parent.bottom)
//                    end.linkTo(parent.end)
//                },
//            icon = { Icon(Icons.Filled.Add, modifier = Modifier.size(20.dp), contentDescription = null) },
//            text = { Text("导入") },
//            onClick = { /*do something*/
//                mainNavHost.navigate("course")}
//        )

    }

}

object WeekList{
    val weekList:List<String> = mutableListOf("一","二","三","四","五","六","日")
}

@Composable
fun Spinner(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dropdownOffsetHeight = 36 // 下拉选项的高度
    val dropdownHeight = ((items.size + 1) * dropdownOffsetHeight).dp.coerceAtMost(240.dp) // 下拉菜单的最大高度

    Box(modifier = Modifier.wrapContentSize()) {
        Text(
            text = selectedItem,
            modifier = Modifier
                .clickable(onClick = { expanded = !expanded })
                .padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(dropdownHeight)
                .widthIn(max = 240.dp),
            offset = DpOffset(0.dp, dropdownOffsetHeight.dp)
        ) {
            items.forEachIndexed {  index,item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    },
                    text = {
                        Text(text = items[index], modifier = Modifier.padding(16.dp))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableMain(modifier: Modifier, week: Int, pageState: PagerState, scrollpos: ScrollState) {
    val courseViewModel: CourseViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
                return CourseViewModel(MyApplication.context.applicationContext as Application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    })
    val timeList by courseViewModel.getCourseTimes().observeAsState()
    val courseList by courseViewModel.getCourses().observeAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    ConstraintLayout(modifier = modifier) {
        val (Date, Time) = createRefs()
        Row(modifier = Modifier
            .wrapContentHeight()
            .constrainAs(Date) {
                top.linkTo(parent.top)
            }) {
            Column(modifier = Modifier.width((screenWidth / 8).dp)) {
                Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "周")
                Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "2023")
            }
            for (i in 0 until 7) {
                Column(modifier = Modifier.width((screenWidth / 8).dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = WeekList.weekList[i]
                    )
                    Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "2023")
                }
            }
        }
//        ConstraintLayout(modifier = Modifier.border(width = 2.dp, color = Color.Blue).){
//
//        }

        ConstraintLayout (
            modifier = Modifier.constrainAs(Time) {
                top.linkTo(Date.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
        ){
            val (table, back, forward) = createRefs()
            val coroutineScope = rememberCoroutineScope()
//            val scrollpos = rememberScrollState()
            Row(modifier = Modifier
                .constrainAs(table) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .nestedScroll(rememberNestedScrollInteropConnection())
                .verticalScroll(scrollpos)
                )
            {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .width((screenWidth / 8).dp)
                )
                {
                    timeList?.let {
                        it.forEach {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((screenHeight / 10).dp)
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .align(Alignment.CenterHorizontally),
                                    text = it.numClass.toString()
                                )
                                Text(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .align(Alignment.CenterHorizontally), text = it.startTime
                                )
                                Text(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .align(Alignment.CenterHorizontally), text = it.endTime
                                )
                            }
                        }
                    }
                }
                courseList?.let {
                    for (day in 0 until 7) {

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width((screenWidth / 8).dp)
                        ) {
                            var skipNum = 0
                            for (node in 0 until 13) {
                                val course =
                                    it.find { it.startWeek <= week && it.endWeek >= week && it.startNode == node + 1 && it.endNode > node && it.day == day + 1 }
                                if (skipNum <= 0) {
                                    if (course != null) {
                                        Log.d("course", course.toString())
//                                skipCount = course.endNode-course.startNode
                                        Card(
                                            modifier = Modifier
                                                .height((screenHeight / 10 * (course.endNode - course.startNode + 1)).dp)
                                                .width((screenWidth / 8).dp)
                                        ) {
                                            Text(
                                                text = course.name,
                                                modifier = Modifier.padding(1.dp),
                                                textAlign = TextAlign.Center,
                                                fontSize = 14.sp
                                            )

                                            Text(
                                                text = course.teacher,
                                                modifier = Modifier.padding(1.dp),
                                                textAlign = TextAlign.Center,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${course.startWeek}-${course.endWeek}",
                                                modifier = Modifier.padding(1.dp),
                                                textAlign = TextAlign.Center,
                                                fontSize = 14.sp
                                            )
                                        }
                                        skipNum = course.endNode - course.startNode

                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .height((screenHeight / 10).dp)
                                                .width((screenWidth / 8).dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(2.dp)
                                                    .background(Color.White)
                                            ) {

                                            }
//                                        Text(
//                                            text = "空闲",
//                                            modifier = Modifier.padding(4.dp),
//                                            fontStyle = FontStyle.Italic,
//                                            color = Color.Gray,
//                                            textAlign = TextAlign.Center
//                                        )
                                        }

                                    }
                                } else {
                                    skipNum--
                                }

                            }
                        }
                    }
                }
            }



        }
    }
}

@Composable
fun SettingList(isMenuOpen: Boolean, onMenuToggle: (Boolean) -> Unit,onSelected: (Int) -> Unit, modifier: Modifier,context: Context) {

    val options = listOf("导入", "Option 2", "Option 3")
    val selectedIndex = remember { mutableStateOf(0) }

    Column (modifier = modifier){
        DropdownMenu(
            modifier = modifier,
            expanded = isMenuOpen,
            onDismissRequest = { onMenuToggle(false) }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(text = { Text(text = option) },onClick = {
                    selectedIndex.value = index
                    onSelected(index)
                    onMenuToggle(false)
                })
            }
        }
    }

}


