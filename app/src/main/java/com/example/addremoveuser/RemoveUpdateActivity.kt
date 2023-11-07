package com.example.addremoveuser

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.addremoveuser.databinding.ActivityUpdateRemoveBinding

class RemoveUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateRemoveBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRemoveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()


    }

    private fun setUp() {
        val user = intent.getParcelableExtra<User>("clickedUser")

        binding.edFirstName.setText(user?.firstName)
        binding.edLastName.setText(user?.lastName)
        binding.edAge.setText(user?.age)
        binding.edEmail.setText(user?.email)

        val updateBtn = binding.btnUpdateUser
        val removeBtn = binding.btnRemoveUser

        removeBtn.setOnClickListener {
            val userForDelete = user?.id?.let {
                User(
                    id = it,
                    firstName = binding.edFirstName.text.toString(),
                    lastName = binding.edLastName.text.toString(),
                    age = binding.edAge.text.toString(),
                    email = binding.edEmail.text.toString()
                )
            }
            val returnIntent = intent.apply {
                putExtra("return_key", userForDelete)
                putExtra("action_key", true)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        updateBtn.setOnClickListener {
            val userForUpdate = user?.id?.let {
                User(
                    id = it,
                    firstName = removeSpaces(binding.edFirstName.text.toString()),
                    lastName = removeSpaces(binding.edLastName.text.toString()),
                    age = removeSpaces(binding.edAge.text.toString()),
                    email = removeSpaces(binding.edEmail.text.toString())
                )
            }
            val returnIntent = intent.apply {
                putExtra("return_key", userForUpdate)
                putExtra("action_key", false)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun removeSpaces(input: String): String {
        return input.replace(" ", "")
    }
}