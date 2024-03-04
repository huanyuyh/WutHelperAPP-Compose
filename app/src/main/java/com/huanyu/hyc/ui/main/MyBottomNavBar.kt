package com.huanyu.hyc.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

data class BottomNavItem(
    val label:String,
    val icon: ImageVector,
    val route:String,
)
object containers{
    val NaviMap:HashMap<Int,String> = hashMapOf(0 to "Table",1 to "Home",2 to "Service",3 to "Message")
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNaviBar(navController: NavHostController, pagerState: PagerState, modifier: Modifier) {
    var currentRoute = remember { mutableStateOf("Home") }
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        val BottomNavItems = listOf(
            BottomNavItem(
                label = "课程表",
                icon = Icons.Filled.DateRange,
                route = "Table"
            ),
            BottomNavItem(
                label = "主页",
                icon = Icons.Filled.Home,
                route = "Home"
            ),
            BottomNavItem(
                label = "校园服务",
                icon = Icons.Filled.Send,
                route = "Service"
            ),
            BottomNavItem(
                label = "资讯",
                icon = Icons.Filled.Notifications,
                route = "Message"
            )
        )
//        var isonclick =
        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        currentRoute.value = navBackStackEntry?.destination?.route.toString()
        currentRoute.value = containers.NaviMap[pagerState.currentPage].toString()

        BottomNavItems.forEach { navItem ->

            BottomNavigationItem(
                modifier = Modifier.fillMaxSize().align(Alignment.Bottom),
//                selected = containers.NaviMap[pagerState.currentPage] == navItem.route,
                selected = currentRoute.value == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)

                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },
                label = {
                    Text(fontSize = 12.sp,text = navItem.label, color = if(currentRoute.value == navItem.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background)
                },
                selectedContentColor = MaterialTheme.colorScheme.secondary,
                alwaysShowLabel = false,
            )

        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavHostContainer(
    navController: NavHostController, pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        // set the start destination as home
        startDestination = "Home",
        // Set the padding provided by scaffold
        modifier = Modifier,
        builder = {
            // route : Table
            composable("Table") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }

            }
            // route : Home
            composable("Home") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }

            }
            // route : Table
            composable("Service") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(2)
                }

            }
            // route : profile
            composable("Message") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(3)
                }
            }
        }
    )
}