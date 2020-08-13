package com.example.githubuserfinder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.UserHolder>() {

    private var users = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = users.size

    fun setUserList(list: List<User>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindItems(users[position])
    }

    inner class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(
            user: User
        ) {
            itemView.tvUsername.text = user.name
            Glide.with(itemView).load(user.avatarUrl).into(itemView.ivAvatar)
        }
    }
}