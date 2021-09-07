package com.example.secondapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.model.Todo
import kotlinx.android.synthetic.main.adapter_todo.view.*

//ViewHolder
class TodoAdapter(val doneClick :(Todo) -> Unit) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    private var todos : MutableList<Todo> = mutableListOf()

    fun setData(data: MutableList<Todo>){
        todos = data
        notifyDataSetChanged()
    }

    fun updateTodo(todo: Todo){
        notifyItemChanged(todos.indexOf(todo))
    }

    fun addTodo(todo: Todo){
        todos.add(todo)
        notifyItemInserted(todos.size -1 )
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.btnDoneTodo.setOnClickListener{
                doneClick(todos[adapterPosition])
            }
        }
        fun bind(todo :Todo){
            if(todo.isCompleted) {
                itemView.txtTodoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            itemView.txtTodoTitle.text = todo.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_todo,parent,false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}