package com.projectinfinity.app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectinfinity.app.R
import com.projectinfinity.app.listeners.StudentAdapterListener
import com.projectinfinity.app.ui.pojo.SingleUserDataResponse
import com.projectinfinity.app.ui.pojo.UserListResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_student.view.*


class StudentClassAdapter(
    //MutableList for storing data
    val data: MutableList<UserListResponse.Data>,
//callback listener
    private val callback: StudentAdapterListener
) : RecyclerView.Adapter<StudentClassAdapter.StudentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_student, parent, false)
        return StudentViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val item = data!![position]
        holder.tvName?.text = item.first_name+" "+item.last_name
        holder.tvClass?.text = item.email
        Picasso.get()
            .load(data[position].avatar)
            .into(holder.image)
        holder.image?.setOnClickListener {
            callback.onImageClick(item.avatar, holder.adapterPosition)
        }
        holder.btnSend?.setOnClickListener {
            callback.onSendClick(item, holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }
    fun dataUpdate(list:List<UserListResponse.Data>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }


    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView? = null
        var tvClass: TextView? = null
        var image: ImageView? = null
        var btnSend: Button? = null

        init {
            tvName = view.tv_username
            tvClass = view.tv_email_item_row_student
            image = view.img_image
            btnSend = view.btn_send
        }

    }
}