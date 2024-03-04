package com.huanyu.hyc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huanyu.connect.Dao.CourseDao
import com.huanyu.connect.Entity.Course
import com.huanyu.hyc.database.Dao.UsersDao
import com.huanyu.hyc.database.Entity.Users

@Database(version = 1, entities = [Users::class],exportSchema = false)
abstract class UsersDatabase: RoomDatabase(){
    public abstract fun usersDao(): UsersDao
    companion object{
        private var instance: UsersDatabase? = null
        @Synchronized
        fun getDatabase(context: Context):UsersDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context,
                UsersDatabase::class.java,"user_database").build().apply {
                instance = this
            }
        }
    }
}