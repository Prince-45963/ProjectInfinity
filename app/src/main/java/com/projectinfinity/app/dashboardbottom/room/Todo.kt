package com.projectinfinity.app.dashboardbottom.room

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @ColumnInfo(name = "task_name") val taskName: String = "",
    @ColumnInfo(name = "task_description") val taskDescription: String = "",
    @ColumnInfo(name = "success") val success: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "todo_id") val todoId: String = "",
    @ColumnInfo(name= "image") val image: String? =""

)
