package com.voitov.todolist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var flag = false

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getShopList().observe(this, Observer {
            Log.d(TAG, it.toString())

            if (!flag) {
                viewModel.editShopItem(it[1].copy(name = "new version"))
                flag = true
            }
        })
    }
}