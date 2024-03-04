package com.huanyu.hyc.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users (
    val platform:String,
    var userName:String,
    var passWord:String,
    var webUrl:String,
    var webJS:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}