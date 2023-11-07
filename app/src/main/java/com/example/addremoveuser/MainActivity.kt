package com.example.addremoveuser

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.addremoveuser.adapter.UsersAdapter
import com.example.addremoveuser.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var isUser: User
    private var activeUsersNum: Int = 0
    private var deletedUsersNum: Int = 0

    private var users = mutableMapOf<UUID, User>()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val user: User? = data?.getParcelableExtra("return_key")
            val action = data?.getBooleanExtra("action_key", false)
            if (action == true) {
                user?.let { removeUser(it.id) }
                adapter.setData(users.values.toList())
                activeUsersNum--
                deletedUsersNum++
            }else{
                user?.let { updateUser(it.id, user) }
                adapter.setData(users.values.toList())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
        onClick(
            addUser = { id, user ->
                addUser(id, user)
                activeUsersNum++
            })
    }

    private fun setUp() {
        val usersRecyclerView = binding.rvList
        adapter = UsersAdapter(listener = {user ->
            val intent = Intent(this, UpdateActivity::class.java).apply {
                putExtra("clickedUser", user)
            }
            startForResult.launch(intent)
        })
        usersRecyclerView.adapter = adapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
    }





    private fun onClick(addUser: (UUID, User) -> Unit) {
        binding.apply {
            btnAddUser.setOnClickListener {
                saveUsers()
                if (checkUser(isUser.id)) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
                } else {
                    addUser(isUser.id, isUser)
                    adapter.setData(users.values.toList())
                    tvActiveUser.text = "Active: $activeUsersNum"
                    Toast.makeText(this@MainActivity, "success", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveUsers() {
        val firstname = binding.edFirstName.text
        val lastname = binding.edLastName.text
        val age = binding.edAge.text
        val email = binding.edEmail.text
        isUser = User(
            firstName = firstname.toString(),
            lastName = lastname.toString(),
            age = age.toString(),
            email = email.toString()
        )
    }

    private fun addUser(id: UUID, user: User) {
        users.put(id, user)
    }

    private fun checkUser(id: UUID): Boolean {
        return users.contains(id)
    }

    private fun removeUser(id: UUID) {
        users.remove(id)
    }

    private fun updateUser(id: UUID, user: User) {
        if (users.contains(id)) {
            users.remove(id)
            users.put(id, user)

        }
    }



}