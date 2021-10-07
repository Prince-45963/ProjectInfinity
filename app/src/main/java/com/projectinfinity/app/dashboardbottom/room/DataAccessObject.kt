package com.projectinfinity.app.dashboardbottom.room

import android.app.ActivityManager
import androidx.room.*

@Dao
interface DataAccessObject {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(todo: Todo)
@Query("SELECT * FROM todo_table")
 fun getAll():MutableList<Todo>
 @Query("DELETE FROM todo_table WHERE todo_id = :id")
 fun deletebyid(id:String)
    @Query("DELETE FROM todo_table")
    fun deleteAll()
    @Update()
    fun update(todo: Todo)
}