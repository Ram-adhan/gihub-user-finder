package com.example.githubuserfinder.ui

import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.GithubUserFinderApplication
import com.example.githubuserfinder.R
import com.example.githubuserfinder.base.BaseActivity
import com.example.githubuserfinder.di.ViewModelFactory
import com.example.githubuserfinder.ui.adapter.UserAdapter
import com.example.githubuserfinder.utils.Resource
import com.example.githubuserfinder.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity(), UserAdapter.OnLoadMoreListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var userAdapter: UserAdapter
    private var page = 1
    private var isLoadMore = false
    private var isCompleted = false

    private var name: String = ""

    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun setupLibrary() {
        super.setupLibrary()
        (application as GithubUserFinderApplication).component.inject(this)
        mainActivityViewModel =
            ViewModelProvider(
                this@MainActivity,
                viewModelFactory
            ).get(MainActivityViewModel::class.java)
    }

    override fun setupUI() {
        super.setupUI()

        val linearLayoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(this, ArrayList())
        userAdapter.linearLayoutManager = linearLayoutManager
        userAdapter.onLoadMoreListener = this
        userAdapter.setRecyclerView(rvUserList)

        rvUserList.layoutManager = linearLayoutManager
        rvUserList.adapter = userAdapter

        showMainInfo(getString(R.string.starting_main_info))
    }

    override fun setupAction() {
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                name = etSearch.text.toString()
                if (name.isNotEmpty()){
                    hideMainInfo()
                    showProgressBar()
                    resetPagination()
                    doLoad()
                }else{
                    showDialogFailed(getString(R.string.query_fault))
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun showProgressBar() {
        pbMainActivity.visibility = View.VISIBLE
    }

    private fun displayToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun hideProgressBar() {
        pbMainActivity.visibility = View.GONE
    }

    private fun hideMainInfo() {
        tvMainInfo.visibility = View.GONE
    }

    private fun showMainInfo(msg: String = getString(R.string.no_user_found)) {
        tvMainInfo.text = msg
        if (userAdapter.itemCount < 1){
            tvMainInfo.visibility = View.VISIBLE
        }else{
            tvMainInfo.visibility = View.GONE
        }
    }

    private fun showDialogFailed(message: String) {
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(getString(R.string.error_title))
        alertDialog.setMessage(message)
        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            this.getText(R.string.dialog_ok)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun resetPagination() {
        page = 1
        isLoadMore = false
        isCompleted = false
        userAdapter.setLoadMoreProgress(isLoadMore)
        userAdapter.clearData()
        userAdapter.setRecyclerView(rvUserList)
    }

    override fun onLoadMore() {
        if (!isCompleted){
            page += 1
            isLoadMore = true
            userAdapter.setLoadMoreProgress(isLoadMore)
            doLoad()
        }
    }

    private fun doLoad(){
        lifecycleScope.launch{
            mainActivityViewModel.getUserList(name, page)
                .observe(this@MainActivity, Observer {
                    hideProgressBar()
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            if (userAdapter.itemCount < 1){
                                showMainInfo()
                            }else{
                                hideMainInfo()
                            }
                            if(!it.data?.items.isNullOrEmpty()){
                                if (isLoadMore){
                                    userAdapter.setLoadMoreProgress(false)
                                }else{
                                    userAdapter.clearData()
                                }
                                it.data?.items?.let { it1 -> userAdapter.addUserList(it1) }
                            }else{
                                isCompleted = true
                                if (isLoadMore){
                                    userAdapter.setLoadMoreProgress(false)
                                    userAdapter.removeScrollListener()
                                }else{
                                    userAdapter.clearData()
                                }
                            }
                        }
                        Resource.Status.LOADING -> {
                        }
                        Resource.Status.ERROR -> {
                            hideMainInfo()
                            when (it.code) {
                                999 -> {
                                    userAdapter.clearData()
                                    showMainInfo(getString(R.string.cannot_reach_server))
                                }
                                403 -> {
                                    showDialogFailed(getString(R.string.common_fault))
                                }
                                in 400..500 -> {
                                    showDialogFailed(getString(R.string.query_fault))
                                }
                            }
                            userAdapter.setLoadMoreProgress(false)
                            userAdapter.removeScrollListener()
                        }
                    }
                })
        }

    }

}