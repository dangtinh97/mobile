package com.example.secondapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.secondapp.fragments.HomeFragment
import com.example.secondapp.model.Todo
import com.example.secondapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_user.*


class MainActivity : AppCompatActivity() {
    lateinit var todoAdapter: TodoAdapter
    lateinit var userAdapter: UserAdapter
    private val homeFragment = HomeFragment()
    private val profileFragment = ProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main) //goi man hinh :D


        /**test data base*/

//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "user"
//        ).build()


        fragmentHome()
//        bottom_navigation.setOnNavigationItemSelectedListener {item->
//            when(item.itemId){
//                R.id.action_home -> {
//                    fragmentHome()
//                    true
//                }
//                R.id.action_profile -> {
//                    fragmentProfile()
//                    true
//                }
//            }
//            true
//
//        }


//        var list = mutableListOf<User>()
//
//        for(i in 1..20){
//            list.add(User(i,"user $i","jwt$i"))
//        }
//
//        userAdapter = UserAdapter()
//
//        list_user.apply {
//            val linearLayoutManager = LinearLayoutManager(context)
//            layoutManager = linearLayoutManager
//            adapter = userAdapter
//        }.run {
//            userAdapter.setData(list)
//        }
//        return
//code test
//        Handler().postDelayed({
//            val myIntent = Intent(this, LoginActivity::class.java)
//            startActivity(myIntent)
//        },2000)
//
//
//        return;
//        setContentView(R.layout.activity_main)
//
//        //<Todo> la class Todo trong model (goi class)
//        val list = mutableListOf<Todo>() //khoi tao mang rong
//
//
//        todoAdapter = TodoAdapter{
//            todo ->  handleDoneClick(todo)
//        }
//
//        //scope function apply, let, run also
//        recyclerView.apply {
//            val linearLayoutManager = LinearLayoutManager(context)
//            layoutManager = linearLayoutManager
//            adapter = todoAdapter
//        }.run {
//            todoAdapter.setData(list)
//        }
//
//        btnAddTodo.setOnClickListener{
//            val todoTitle = editTodoTitle.text.toString().trim();
//            if(todoTitle.isEmpty()) return@setOnClickListener
//
//            var todo = Todo(todoTitle)
//            todoAdapter.addTodo(todo)
//            editTodoTitle.text.clear();
//        }
//        //object expression thay doi object
//        todoAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                recyclerView.smoothScrollToPosition(todoAdapter.itemCount - 1)
//            }
//        })
    }

//    private fun replaceFragment(fragment: Fragment){
//        if(fragment != null){
//            supportFragmentManager.commit {
//                replace<Fragment>(R.id.frame)
//                setReorderingAllowed(true)
//                addToBackStack("name") // name can be null
//            }
//        }
//    }

    private fun fragmentHome (){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view_tag,homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fragmentProfile (){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view_tag,profileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fragmentSetup (){
        println("Vao day di ma")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view_tag,homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun handleDoneClick(todo: Todo){
        todo.isCompleted = true
        todoAdapter.updateTodo(todo)
    }
}