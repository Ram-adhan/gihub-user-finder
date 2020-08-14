package com.example.githubuserfinder.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.model.User
import kotlinx.android.synthetic.main.item_data_loading.view.*
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private val context: Context, private var users: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOAD_MORE = 0
        const val VIEW_TYPE_ITEM = 1
        val LOAD_MORE_ITEM = User("", "")
    }

    private var isLoadMoreLoading = false

    var linearLayoutManager: LinearLayoutManager? = null
    var onLoadMoreListener: OnLoadMoreListener? = null
    private var recyclerView: RecyclerView? = null
    private var scrollListener: RecyclerView.OnScrollListener? = null

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 0
                val totalItemCount: Int = linearLayoutManager!!.itemCount
                val lastVisibleItem: Int = linearLayoutManager!!.findLastVisibleItemPosition() + 1

                if (!isLoadMoreLoading && totalItemCount >= 10 && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener?.onLoadMore()
                        isLoadMoreLoading = true
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(scrollListener as RecyclerView.OnScrollListener)
    }

    fun removeScrollListener() {
        scrollListener?.let { recyclerView?.removeOnScrollListener(it) }
    }

    fun setLoadMoreProgress(isProgress: Boolean) {
        isLoadMoreLoading = isProgress
        if (isProgress) {
            users.add(users.size, LOAD_MORE_ITEM)
        } else {
            if (users.size > 0) {
                users.remove(LOAD_MORE_ITEM)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (users[position] != LOAD_MORE_ITEM) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_LOAD_MORE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            UserHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_user, parent, false)
            )
        } else {
            LoadMoreViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_data_loading, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return try {
            users.size
        } catch (e: Exception){
            Log.d("userAdapter", "an exception: ${e.localizedMessage}")
            0
        }
    }

    fun setUserList(list: List<User>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData(){
        users.clear()
        notifyDataSetChanged()
    }

    fun addUserList(list: List<User>){
        users.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(users[position])
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindItems(user: User)
    }

    inner class UserHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindItems(
            user: User
        ) {
            itemView.tvUsername.text = user.name
            Glide.with(itemView).load(user.avatarUrl).into(itemView.ivAvatar)
        }
    }

    inner class LoadMoreViewHolder(
        itemView: View
    ) : ViewHolder(itemView) {

        override fun bindItems(user: User) {
            if (isLoadMoreLoading) {
                itemView.pbItemLoading.visibility = View.VISIBLE
            } else {
                itemView.pbItemLoading.visibility = View.GONE
            }
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}