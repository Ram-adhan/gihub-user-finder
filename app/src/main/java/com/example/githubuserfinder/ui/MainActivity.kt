package com.example.githubuserfinder.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserfinder.GithubUserFinderApplication
import com.example.githubuserfinder.R
import com.example.githubuserfinder.base.BaseActivity
import com.example.githubuserfinder.di.ViewModelFactory
import com.example.githubuserfinder.model.User
import com.example.githubuserfinder.ui.adapter.UserAdapter
import com.example.githubuserfinder.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var userAdapter: UserAdapter

    private var page = 1

    override fun setupProcess() {
        super.setupProcess()

        val linearLayoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter()
        rvUserList.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }

    override fun setupAction() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                lifecycleScope.launch {
                    mainActivityViewModel.getUserList(etSearch.text.toString(), page)
                        .observe(this@MainActivity, Observer {
                            when (it.status) {
                                Resource.Status.SUCCESS -> {
                                    displayToast("Success")
                                    it.data?.items?.let { it1 -> userAdapter.setUserList(it1) }
                                }
                                Resource.Status.LOADING -> {
                                    displayToast("on Loading")
                                }
                                Resource.Status.ERROR -> {
                                    displayToast("error getting data")
                                }
                            }
                        })
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


    }

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun setupLibrary() {
        super.setupLibrary()
        initInject()
        initViewModel()
    }

    private fun initViewModel() {
        mainActivityViewModel =
            ViewModelProvider(
                this@MainActivity,
                viewModelFactory
            ).get(MainActivityViewModel::class.java)
    }

    private fun initInject() {
        (application as GithubUserFinderApplication).component.inject(this)
    }

    private fun displayToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}