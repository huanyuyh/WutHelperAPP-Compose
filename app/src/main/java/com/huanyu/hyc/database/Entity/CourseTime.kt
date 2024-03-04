package com.huanyu.hyc.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class CourseTime(
    val numClass:Int,
    val startTime: String,
    val endTime:String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}