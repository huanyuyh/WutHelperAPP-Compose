package com.huanyu.hyc.Utils

import android.content.Context
import com.huanyu.connect.Entity.Course

import com.huanyu.hyc.Utils.FileUtil.Companion.readText

class WutTools {
    companion object{
        fun readCourses(context: Context): List<Course>? {
            var courseList:List<Course>? = null
            var temp = readText(context.externalCacheDir?.path+"/jwc.html")
            var wutParser = temp?.let { WUTParser(it) }
            wutParser?.let {
                courseList = it.generateCourseList()
            }

            return courseList
        }

    }

}