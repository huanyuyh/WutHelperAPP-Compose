package com.huanyu.hyc

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.huanyu.connect.tools.CourseDatabase
import com.huanyu.hyc.Utils.SharedPreferenceUtil
import com.huanyu.hyc.database.Entity.CourseTime
import com.huanyu.hyc.database.Entity.Users
import com.huanyu.hyc.database.UsersDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication:Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    companion object{
        lateinit var context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        applicationScope.launch {
            val sharedPreferenceUtil = SharedPreferenceUtil(this@MyApplication)
            Log.d("MYAPP",sharedPreferenceUtil.getBoolean("firstStart",false).toString())
            if(!sharedPreferenceUtil.getBoolean("firstStart",false)){
                val usersDatabase = UsersDatabase.getDatabase(this@MyApplication)
                val courseDatabase = CourseDatabase.getDatabase(this@MyApplication)
                courseDatabase.courseTimeDao().clearCourseTime()
                val courseTimes:List<CourseTime> = mutableListOf(
                    CourseTime(1,"08:00","08:45"),
                    CourseTime(2,"08:50","09:35"),
                    CourseTime(3,"09:55","10:40"),
                    CourseTime(4,"10:45","11:30"),
                    CourseTime(5,"11:35","12:20"),
                    CourseTime(6,"14:00","14:45"),
                    CourseTime(7,"14:50","15:35"),
                    CourseTime(8,"15:40","16:25"),
                    CourseTime(9,"16:45","17:30"),
                    CourseTime(10,"17:35","16:20"),
                    CourseTime(11,"19:00","19:45"),
                    CourseTime(12,"19:50","20:35"),
                    CourseTime(13,"20:40","21:25"),
                )
                courseDatabase.courseTimeDao().insertTimeCourseTimes(courseTimes)
                usersDatabase.usersDao().clearUser()
                var usersList:List<Users> = mutableListOf(
                    Users("智慧理工大",
                        "0122109361613",
                        "nyh314nyh",
                        "http://zhlgd.whut.edu.cn/",
                        "document.getElementById('un').value = '0122109361613';\n" +
                            "document.getElementById('pd').value = 'nyh314nyh';\n" +
                            "document.getElementById('index_login_btn').click();\n"),
                    Users("教务系统（智慧理工大）",
                        "0122109361613",
                        "nyh314nyh",
                        "http://sso.jwc.whut.edu.cn/Certification/index2.jsp",
                        "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"),
                    Users("教务系统",
                        "0122109361613",
                        "nyh314nyh",
                        "http://sso.jwc.whut.edu.cn/Certification/toIndex.do",
                        "document.getElementById('username').value = '0122109361613';\n" +
                                "document.getElementById('password').value = 'NYH3!14nyh';\n" +
                                "setTimeout(function() {\n" +
                                "document.getElementById('submit_id').click()\n" +
                                "}, 1000);\n"

                                ),
                    Users("教务系统(新)",
                        "0122109361613",
                        "nyh314nyh",
                        "http://jwxt.whut.edu.cn",
                        "document.getElementById('tyrzBtn').click();\n"+
                                "setTimeout(function() {\n" +
                                "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"+
                                "}, 1000);\n"
                       ),
                    Users("缴费平台",
                        "0122109361613",
                        "nyh314nyh",
                        "http://cwsf.whut.edu.cn",
                        "document.getElementById('usercode').value = '0122109361613'\n" +
                                "document.getElementById('passwd').value = 'nyh314nyh'\n" +
                                "formSubmit();\n"),
                    Users("智慧学工",
                        "0122109361613",
                        "nyh314nyh",
                        "https://talent.whut.edu.cn/",
                        "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"),
                    Users("校园地图（智慧理工大版）",
                        "0122109361613",
                        "nyh314nyh",
                        "http://gis.whut.edu.cn/index.shtml",
                        "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"),
                    Users("校园地图（微校园版）",
                        "0122109361613",
                        "nyh314nyh",
                        "http://gis.whut.edu.cn/mobile/index.html#/",
                        "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"),
                    Users("WebVPN",
                        "0122109361613",
                        "nyh314nyh",
                        "https://webvpn.whut.edu.cn/",
                        "document.getElementsByName('username')[0].value = '329115'\n" +
                                "document.getElementsByName('password')[0].value = 'nyh314nyh'\n" +
                                "document.getElementsByName('remember_cookie')[0].click()\n" +
                                "document.getElementById('login').click()\n"),
                    Users("学校邮箱",
                        "0122109361613",
                        "nyh314nyh",
                        "https://qy.163.com/login/",
                        "document.getElementById('accname').value = '329115@whut.edu.cn'\n" +
                                "document.getElementById('accpwd').value = 'NYH314nyh'\n" +
                                "document.getElementById('accautologin').checked\n" +
                                "document.getElementsByClassName('u-logincheck logincheck js-logincheck js-loginPrivate loginPrivate')[0].click()\n" +
                                "document.getElementsByClassName('w-button w-button-account js-loginbtn')[0].click()\n"),
                    Users("成绩查询",
                        "0122109361613",
                        "nyh314nyh",
                        "http://zhlgd.whut.edu.cn/tp_up/view?m=up#act=up/sysintegration/queryGrade",
                        "document.getElementById('un').value = '0122109361613'\n" +
                                "document.getElementById('pd').value = 'nyh314nyh'\n" +
                                "document.getElementById('index_login_btn').click()\n"),
                    Users("校园网认证(1.1.1.1)",
                        "0122109361613",
                        "nyh314nyh",
                        "http://1.1.1.1",
                        "document.getElementById('username').value = '329115'\n" +
                                "document.getElementById('password').value = 'nyh314nyh'\n" +
                                "checkForm()\n"),
                    Users("校园网认证(WLAN)",
                        "0122109361613",
                        "nyh314nyh",
                        "http://172.30.21.100",
                        "document.getElementById('username').value = '329115'\n" +
                                "document.getElementById('password').value = 'nyh314nyh'\n" +
                                "checkForm()\n"),
                    Users("校园网认证(isp)",
                        "0122109361613",
                        "nyh314nyh",
                        "http://172.30.21.100/",
                        "document.getElementById('username').value = '329115'\n" +
                                "document.getElementById('password').value = 'nyh314nyh'\n" +
                                "checkForm()\n"),
                )
                usersList.forEach {
                    usersDatabase.usersDao().insertUser(it)
                }
                val users2 = usersDatabase.usersDao().loadAllUser()
                users2.value?.forEach { Log.d("MyAPP",it.toString()) }
//                users2.forEach {
//                    Log.d("MyAPP",it.toString())
//                }
                sharedPreferenceUtil.putBoolean("firstStart",false)
            }
        }
    }

}