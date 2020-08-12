package com.example.githubuserfinder.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.githubuserfinder.GithubUserFinderApplication
import com.example.githubuserfinder.R
import com.example.githubuserfinder.di.ViewModelFactory
import com.example.githubuserfinder.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInject()
        initViewModel()

        setContentView(R.layout.activity_main)

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                lifecycleScope.launch {
                    mainActivityViewModel.getUserList(etSearch.text.toString(), page)
                        .observe(this@MainActivity, Observer {
                            when (it.status) {
                                Resource.Status.SUCCESS -> {
                                    displayToast("Success")
                                }
                                Resource.Status.LOADING -> {}
                                Resource.Status.ERROR -> {}
                            }
                        })
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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

    private fun displayToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}