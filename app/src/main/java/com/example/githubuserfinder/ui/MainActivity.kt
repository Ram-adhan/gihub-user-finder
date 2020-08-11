package com.example.githubuserfinder.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserfinder.R
import com.example.githubuserfinder.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                hideKeyboard()
                Toast.makeText(this,"Start searching", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

}