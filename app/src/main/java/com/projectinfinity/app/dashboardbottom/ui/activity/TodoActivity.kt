package com.projectinfinity.app.dashboardbottom.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.ui.fragment.ToDoFragment

class TodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        //added todoFragment with bundle
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            val bundle = bundleOf(
                "todoId" to intent?.getStringExtra("todoId"),
                "taskName" to intent?.getStringExtra("taskName"),
                "taskDescription" to intent?.getStringExtra("taskDescription"),
                "taskStatus" to intent?.getBooleanExtra("taskStatus", false),
                "uid" to intent?.getStringExtra("uid"),
                "image" to intent?.getStringExtra("image")
            )
            add<ToDoFragment>(R.id.fcv_activity_TodoActivity, args = bundle)

        }
    }

}