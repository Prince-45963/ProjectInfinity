package com.projectinfinity.app.dashboardbottom.ui.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.projectinfinity.app.R
import com.projectinfinity.app.dashboardbottom.room.AppDatabase
import com.projectinfinity.app.dashboardbottom.room.Todo
import com.projectinfinity.app.dashboardbottom.ui.activity.TodoActivity
import com.projectinfinity.app.listeners.DialogShowListener
import com.projectinfinity.app.listeners.TodoListAdapterListener
import com.projectinfinity.app.ui.adapter.TodoListAdapter
import com.projectinfinity.app.utils.GeneralUtils.databaseReference
import com.projectinfinity.app.utils.GeneralUtils.displayToast
import com.projectinfinity.app.utils.GeneralUtils.firebaseAuth
import com.projectinfinity.app.utils.GeneralUtils.generalDialog
import com.projectinfinity.app.utils.GeneralUtils.getLoader
import com.projectinfinity.app.utils.GeneralUtils.isConnectionAvailable
import kotlinx.android.synthetic.main.fragment_color_list.*


class TodoListFragment : Fragment(R.layout.fragment_color_list) {
    //put TodoListAdapterListener in variable todoListListener and overriding its method
    private val todoListListener = object : TodoListAdapterListener {
        override fun onDeleteClick(todoId: String, adapterPosition: Int) {
            //show dialog on delete
            deleteTodo(adapterPosition, todoId)
        }

        override fun onEditClick(
            data: MutableList<Todo>,
            adapterPosition: Int,
            uid: String,
        ) {

            //Sending intent with data and TodoActivity
            val intent =
                Intent(requireContext(), TodoActivity::class.java).putExtra(
                    "todoId",
                    data[adapterPosition].todoId
                )
                    .putExtra("taskName", data[adapterPosition].taskName)
                    .putExtra("taskDescription", data[adapterPosition].taskDescription)
                    .putExtra("taskStatus", data[adapterPosition].success).putExtra("uid", uid).putExtra("image",data[adapterPosition].image)
            //starting activity
            startActivity(intent)
        }

    }

    //function for showing dialog on delete
    private fun deleteTodo(adapterPosition: Int, todoId: String) {
        requireContext().generalDialog(
            "Delete",
            "Do You really want to delete?",
            object : DialogShowListener {
                //if click on positive button
                override fun setPositiveButton(title: String, message: String) {
                    //data will delete will delete from firebase
                    databaseReference().child("todo").child(firebaseAuth().uid!!)
                        .child(todoId)
                        .removeValue()
                    //data will delete will delete from RoomDatabase
                    AsyncTask.execute {
                        AppDatabase.getDatabase(requireContext()).todoDao()
                            .deletebyid(todoId)
                    }
                    requireContext().displayToast("deleted")
                    todoAdapter.removeItem(adapterPosition)

                }

                //if click on negative button
                override fun setNegativeButton() {

                }

            })

    }

    val todoAdapter = TodoListAdapter(
        mutableListOf<Todo>(), todoListListener
    )
    //on Resume
    override fun onResume() {
        super.onResume()
        val llm = LinearLayoutManager(requireContext())
        rv_color_list.layoutManager = llm
        getTodoList()
        rv_color_list.adapter = todoAdapter
        toolbar_dashboard_bottom.title = getString(R.string.color_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if click of floating action button then new fragment will open for adding new
        fab_color_list_fragment.setOnClickListener {

            //TodoActivity is adding
            val intent = Intent(requireContext(), TodoActivity::class.java)
            startActivity(intent)
        }

    }

    //function for gets the list
    private fun getTodoList() {
        //creating loader
        val loader = requireContext().getLoader(getString(R.string.loader_loading))
        //loader showing
        loader.show()
        var dataList = mutableListOf<Todo>()
        //if internet connection is on TodoList of firebase shows
        //else TodoList shows from room Database
        if (isConnectionAvailable(requireContext())) {
            //getting list from firebase
            databaseReference().child("todo").child(firebaseAuth().uid!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        loader.hide()
                        dataList.clear()
                        snapshot.children.forEach {
                            var todoData = it.getValue(Todo::class.java)
                            dataList.add(todoData!!)
                        }
                        todoAdapter.TodoDataUpdate(dataList)


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
                )
        } else {
            //loader hide
            loader.hide()
            dataList.clear()
            //AsyncTask will execute
            AsyncTask.execute {
                //getAll TodoList from roomDatabase
                dataList =
                    AppDatabase.getDatabase(requireContext()).todoDao().getAll()
                //passing dataList to adapter
                todoAdapter.TodoDataUpdate(dataList)
            }
        }

    }


}



