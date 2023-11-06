package com.example.addremoveuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import android.widget.EditText
import android.widget.Toast
import com.example.addremoveuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var isUser: User
    private var activeUsersNum: Int = 0
    private var deletedUsersNum: Int = 0

    private var users = mutableMapOf<String, User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick(
            addUser = { email, user ->
                addUser(email, user)
                activeUsersNum++
            },
            removeUser = { user ->
                removeUser(user.email)
                activeUsersNum--
                deletedUsersNum++
            })

    }

    private fun onClick(addUser: (String, User) -> Unit, removeUser: (User) -> Unit) {
        binding.apply {
            btnAddUser.setOnClickListener {
                saveUsers()
                if (checkUser(isUser.email)) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
                } else {
                    addUser(isUser.email, isUser)
                    tvActiveUser.text = "Active: $activeUsersNum"
                    Toast.makeText(this@MainActivity, "success", Toast.LENGTH_LONG).show()
                }
                d("users", "${users.size}")
            }
            btnRemoveUser.setOnClickListener {
                if (checkUser(isUser.email)) {
                    removeUser(isUser)
                    tvActiveUser.text = "Active: $activeUsersNum"
                    tvDeleteUser.text = "Delete: $deletedUsersNum"
                    Toast.makeText(this@MainActivity, "removed", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "not removed", Toast.LENGTH_LONG).show()
                }
            }
            btnUpdateUser.setOnClickListener {
                if (checkUser(isUser.email)) {
                    updateUser(isUser.email, isUser)
                    Toast.makeText(this@MainActivity, "removed", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "not removed", Toast.LENGTH_LONG).show()
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

    // add user
    private fun addUser(email: String, user: User) {
        users.put(email, user)
    }

    private fun checkUser(email: String): Boolean {
        return users.contains(email)
    }

    // Удаление пользователя
    private fun removeUser(email: String) {
        users.remove(email)
    }

    //    // Обновление данных пользователя
    fun updateUser(email: String, user: User) {
        if (users.contains(email)) {
            users.remove(email)
            users.put(email, user)
        }
    }


}