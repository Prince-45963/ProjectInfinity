package com.projectinfinity.app.listeners

import com.projectinfinity.app.dashboardbottom.room.Todo

interface TodoListAdapterListener {
    fun onDeleteClick(todoId: String, adapterPosition: Int)
    fun onEditClick(
        data: MutableList<Todo>,
        adapterPosition: Int,
        uid: String
    )
}