package com.example.addremoveuser

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.addremoveuser.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = intent.getParcelableExtra<User>("clickedUser")

        binding.edFirstName.setText(user?.firstName)
        binding.edLastName.setText(user?.lastName)
        binding.edAge.setText(user?.age)
        binding.edEmail.setText(user?.email)

        val updateBtn = binding.btnUpdateUser
        val removeBtn = binding.btnRemoveUser

        removeBtn.setOnClickListener {
            val user1 = user?.id?.let {
                User(
                    id = it,
                    firstName = binding.edFirstName.text.toString(),
                    lastName = binding.edLastName.text.toString(),
                    age = binding.edAge.text.toString(),
                    email = binding.edEmail.text.toString()
                )
            }
            val returnIntent = intent.apply {
                putExtra("return_key", user1)
                putExtra("action_key", true)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        updateBtn.setOnClickListener {
            val user1 = user?.id?.let {
                User(
                    id = it,
                    firstName = binding.edFirstName.text.toString(),
                    lastName = binding.edLastName.text.toString(),
                    age = binding.edAge.text.toString(),
                    email = binding.edEmail.text.toString()
                )
            }
            val returnIntent = intent.apply {
                putExtra("return_key", user1)
                putExtra("action_key", false)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}