package com.example.addremoveuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.addremoveuser.R
import com.example.addremoveuser.User
import com.example.addremoveuser.databinding.UsersListBinding

class UsersAdapter(private val listener: (User) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UsersHolder>() {

    private val usersList = ArrayList<User>()


    inner class UsersHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UsersListBinding.bind(view)

        fun setUser(user: User) {
            binding.tvFirstNameValue.text = user.firstName
            binding.tvLastNameValue.text = user.lastName
            binding.tvAgeValue.text = user.age
            binding.tvEmailValue.text = user.email
            binding.viewHolder.setOnClickListener {
                listener(user)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_list, parent, false)
        return UsersHolder(view)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        val currentUser = usersList[position]
        holder.setUser(currentUser)
    }

    fun setData(userList: List<User>) {
        usersList.clear()
        usersList.addAll(userList)
        notifyDataSetChanged()
    }

}
