package com.huanyu.connect.tools

import android.content.Context
import androidx.compose.runtime.CompositionLocalContext
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huanyu.connect.Dao.CourseDao
import com.huanyu.connect.Dao.CourseTimeDao
import com.huanyu.connect.Entity.Course
import com.huanyu.hyc.database.Entity.CourseTime

@Database(version = 1, entities = [Course::class,CourseTime::class],exportSchema = false)
abstract class CourseDatabase: RoomDatabase(){
    public abstract fun courseDao():CourseDao
    public abstract fun courseTimeDao(): CourseTimeDao
    companion object{
        private var instance: CourseDatabase? = null
        @Synchronized
        fun getDatabase(context: Context):CourseDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context,
                CourseDatabase::class.java,"course_database").build().apply {
                    instance = this
            }
        }
    }
}