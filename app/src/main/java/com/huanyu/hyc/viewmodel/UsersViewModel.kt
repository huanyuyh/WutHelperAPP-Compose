package com.huanyu.hyc.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huanyu.connect.Entity.Course
import com.huanyu.connect.tools.CourseDatabase
import com.huanyu.hyc.database.Dao.UsersDao
import com.huanyu.hyc.database.Entity.Users
import com.huanyu.hyc.database.UsersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel(context: Context): ViewModel(){

    var usersDatabase: UsersDatabase = UsersDatabase.getDatabase(context)
//    fun setDataBase(context: Context){
//        viewModelScope.launch(Dispatchers.IO) {
//            usersDatabase = UsersDatabase
//        }
//
//    }
    fun getUsers(): LiveData<List<Users>> {
//        var  usersList:LiveData<List<Users>>? = null
//        viewModelScope.launch(Dispatchers.IO) {
////            Log.d("view","ok")
////            usersDatabase.usersDao().loadAllUser().value?.forEach {
////                Log.d("view",it.toString())
////            }
//
////            usersDatabase?.let {
////                usersList = usersDatabase!!.usersDao().loadAllUser()
////            }
//
//        }
//        usersList?.value?.forEach {
//            Log.d("view",it.toString())
//        }

        return usersDatabase.usersDao().loadAllUser()
    }
    fun searchUsers(platform:String): LiveData<Users> {
//        var user:LiveData<Users>? = null
//        viewModelScope.launch(Dispatchers.IO) {
//            user = usersDatabase.usersDao().searchPlatform(platform)
//            Log.d("base",user.toString())
//        }
        return usersDatabase.usersDao().searchPlatform(platform)
    }
    fun insertUser(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            usersDatabase.usersDao().insertUser(users)


        }
    }

    fun updateUser(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            usersDatabase.usersDao().updateUser(users)


        }
    }
    fun deleteUser(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            usersDatabase.usersDao().deleteUser(users)


        }
    }
    fun clearUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            usersDatabase.usersDao().clearUser()


        }
    }
}