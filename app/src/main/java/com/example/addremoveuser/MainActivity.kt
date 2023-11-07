package com.example.addremoveuser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    @SuppressLint("ResourceAsColor")
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
                binding.tvActiveUser.text = getString(R.string.active_users) + " " + activeUsersNum
                binding.tvDeleteUser.text = getString(R.string.deleted_users) + " " + deletedUsersNum
                binding.tvTitle.text = getString(R.string.success)
                binding.tvTitle.setTextColor(Color.GREEN)
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_LONG).show()

            }else{
                user?.let { updateUser(it.id, user) }
                adapter.setData(users.values.toList())
                binding.tvTitle.text = getString(R.string.success)
                binding.tvTitle.setTextColor(Color.GREEN)
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_LONG).show()

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
                binding.tvActiveUser.text = getString(R.string.active_users) + activeUsersNum
                binding.tvTitle.text = getString(R.string.success)
                binding.tvTitle.setTextColor(Color.GREEN)
            })
    }

    private fun setUp() {
        binding.tvActiveUser.text = getString(R.string.active_users) + " " + activeUsersNum
        binding.tvDeleteUser.text = getString(R.string.deleted_users) + " " + deletedUsersNum
        val usersRecyclerView = binding.rvList
        adapter = UsersAdapter(listener = {user ->
            val intent = Intent(this, RemoveUpdateActivity::class.java).apply {
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

                if (edFirstName.text.isNullOrEmpty() || edLastName.text.isNullOrEmpty() || edAge.text.isNullOrEmpty() || edEmail.text.isNullOrEmpty() ) {
                    binding.tvTitle.text = getString(R.string.error)
                    binding.tvTitle.setTextColor(Color.RED)
                }else {
                    if (checkUser(isUser)) {
                        binding.tvTitle.text = getString(R.string.error)
                        binding.tvTitle.setTextColor(Color.RED)
                        Toast.makeText(this@MainActivity, "User already exists", Toast.LENGTH_LONG).show()
                    } else {
                        addUser(isUser.id, isUser)
                        adapter.setData(users.values.toList())
                        Toast.makeText(this@MainActivity, "User added successfully", Toast.LENGTH_LONG).show()
                    }
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
            firstName = removeSpaces(firstname.toString()),
            lastName = removeSpaces(lastname.toString()),
            age = removeSpaces(age.toString()),
            email = removeSpaces(email.toString())
        )
    }


    private fun addUser(id: UUID, user: User) {
        users.put(id, user)
    }

    private fun checkUser(user: User): Boolean {
        var boolean = false
        users.values.forEach{
            boolean = it.email == user.email
        }
        return boolean
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

    private fun removeSpaces(input: String): String {
        return input.replace(" ", "")
    }

}