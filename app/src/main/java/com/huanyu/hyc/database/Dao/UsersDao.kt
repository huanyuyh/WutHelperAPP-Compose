package com.huanyu.hyc.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.huanyu.connect.Entity.Course
import com.huanyu.hyc.database.Entity.Users

@Dao
interface UsersDao {
    @Insert
    fun insertUser(vararg User: Users)
    @Update
    fun updateUser(newUser: Users)
    @Query("select * from Users")
    fun loadAllUser(): LiveData<List<Users>>
    @Delete
    fun deleteUser(User: Users)
    @Query("delete from Users where 1=1")
    fun clearUser()
    @Query("select * from Users where platform = :platform Limit 1")
    fun searchPlatform(platform:String):LiveData<Users>
}