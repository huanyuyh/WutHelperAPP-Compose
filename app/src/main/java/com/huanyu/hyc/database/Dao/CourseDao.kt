package com.huanyu.connect.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.huanyu.connect.Entity.Course

@Dao
interface CourseDao {
    @Insert
    fun insertCourse(course: Course): Long
    @Update
    fun updateCourse(newCourse: Course)
    @Query("select * from Course")
    fun loadAllCourse(): LiveData<List<Course>>
    @Delete
    fun deleteCourse(course: Course)
    @Query("delete from Course where 1=1")
    fun clearCourse()

}