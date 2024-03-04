package com.huanyu.connect.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.huanyu.connect.Entity.Course
import com.huanyu.hyc.database.Entity.CourseTime

@Dao
interface CourseTimeDao {
    @Insert
    fun insertTimeCourseTime(courseTime: CourseTime): Long
    @Insert
    fun insertTimeCourseTimes(courseTime: List<CourseTime>)
    @Update
    fun updateCourseTime(newcourseTime: CourseTime)
    @Query("select * from CourseTime order by numClass")
    fun loadAllCourseTime(): LiveData<List<CourseTime>>
    @Delete
    fun deleteCourseTime(courseTime: CourseTime)
    @Query("delete from CourseTime where 1=1")
    fun clearCourseTime()

}