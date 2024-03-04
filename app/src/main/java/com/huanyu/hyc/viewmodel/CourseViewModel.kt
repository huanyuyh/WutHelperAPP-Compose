package com.huanyu.hyc.viewmodel

import android.content.Context
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huanyu.connect.Entity.Course
import com.huanyu.connect.tools.CourseDatabase
import com.huanyu.hyc.database.Entity.CourseTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class CourseViewModel(context: Context): ViewModel() {


    var courseDatabase:CourseDatabase? = CourseDatabase.getDatabase(context)
//    fun setDataBase(context: Context){
//        viewModelScope.launch(Dispatchers.IO) {
//            courseDatabase = CourseDatabase.getDatabase(context)
//        }
//
//    }
    fun getCourses(): LiveData<List<Course>> {

        return courseDatabase!!.courseDao().loadAllCourse()
    }
    fun getCourseTimes(): LiveData<List<CourseTime>> {

        return courseDatabase!!.courseTimeDao().loadAllCourseTime()
    }
    fun insertCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDatabase?.let {
                courseDatabase!!.courseDao().insertCourse(course)
            }

        }
    }

    fun updateCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDatabase?.let {
                courseDatabase!!.courseDao().updateCourse(course)
            }

        }
    }
    fun deleteCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            courseDatabase?.let {
                courseDatabase!!.courseDao().deleteCourse(course)
            }

        }
    }
    fun clearCourse() {
        viewModelScope.launch(Dispatchers.IO) {
            courseDatabase?.let {
                courseDatabase!!.courseDao().clearCourse()
            }

        }
    }



}