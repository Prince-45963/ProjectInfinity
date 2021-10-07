package com.projectinfinity.app.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.room.Todo
import com.projectinfinity.app.listeners.TodoListAdapterListener
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_todo.view.*

import java.security.Provider


class TodoListAdapter(val data:MutableList<Todo>, private val callback:TodoListAdapterListener):RecyclerView.Adapter<TodoListAdapter.ColorViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_todo, parent, false)
        return ColorViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = data[position]
        holder.tvName?.text = item.taskName
        holder.tvDesc?.text=item.taskDescription
        if(item.image!=""){
        Picasso.get().load(Uri.parse(item.image)).into(holder.ivProfilePicture)
        }


        if(!item.success) {
            holder.tvStatus?.text = "Pending"
            holder.tvStatus?.setTextColor(Color.parseColor("#ff0000"))
        }
        else{
            holder.tvStatus?.text = "Completed"
            holder.tvStatus?.setTextColor(Color.parseColor("#00FF00"))
        }
        holder.ivDelete?.setOnClickListener {
            callback.onDeleteClick(item.todoId,holder.adapterPosition)
        }
        holder.ivEdit?.setOnClickListener {
            callback.onEditClick(data,holder.adapterPosition,firebaseAuth().uid!!)
        }


    }

    override fun getItemCount(): Int {
        return data!!.size
    }


    fun TodoDataUpdate(todo1: MutableList<Todo>){

        data.clear()
        data.addAll(todo1)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        this.data.removeAt(position)
        notifyItemRemoved(position)
    }
    fun updateItem(todo: MutableList<Todo>){
        data.clear()
        data.addAll(todo)
notifyDataSetChanged()    }


    class ColorViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvName:TextView?=null
        var tvDesc:TextView?=null
        var tvStatus:TextView?=null
        var ivDelete:ImageView?=null
        var ivEdit:ImageView?=null
        var ivProfilePicture:ImageView?=null
        init{
            tvName=view.tv_name_item_row_todo
            tvDesc=view.tv_description_item_row_todo
            tvStatus=view.tv_status_item_row_todo
            ivDelete=view.iv_delete_todo_item_row_todo
            ivEdit=view.iv_edit_todo_item_row_todo
            ivProfilePicture=view.iv_profile_picture_item_row_todo
        }

    }
}