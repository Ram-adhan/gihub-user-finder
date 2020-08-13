package com.example.githubuserfinder.ui

import android.os.Bundle
import android.view.View
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
import com.example.githubuserfinder.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var userAdapter: UserAdapter

    private var page = 1

    override fun getLayoutResource(): Int = R.layout.activity_main

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

    override fun setupUI() {
        super.setupUI()
        showMainInfo(getString(R.string.starting_main_info))
    }


    override fun setupAction() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showProgressBar()
                hideKeyboard()
                lifecycleScope.launch {
                    mainActivityViewModel.getUserList(etSearch.text.toString(), page)
                        .observe(this@MainActivity, Observer {
                            hideProgressBar()
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

    private fun showProgressBar(){
        hideMainInfo()
        pbMainActivity.visibility = View.VISIBLE
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

    private fun hideProgressBar(){
        pbMainActivity.visibility = View.GONE
    }

    private fun hideMainInfo(){
        tvMainInfo.visibility = View.GONE
    }

    private fun showMainInfo(msg: String = getString(R.string.no_user_found)){
        tvMainInfo.text = msg
        tvMainInfo.visibility = View.VISIBLE
    }

    override fun setupLibrary() {
        super.setupLibrary()
        initInject()
        initViewModel()
    }

}