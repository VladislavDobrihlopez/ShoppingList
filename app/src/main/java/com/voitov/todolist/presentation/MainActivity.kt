package com.voitov.todolist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.voitov.todolist.R
import com.voitov.todolist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var linearLayoutShopList: LinearLayout
    private lateinit var floatingActionButtonAddShopItem: FloatingActionButton
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getShopList().observe(this, Observer {
            Log.d(TAG, it.toString())
            //showShopList(it)
            adapter.shopItems = it
        })
    }

    private fun initViews() {
        linearLayoutShopList = findViewById(R.id.linearLayoutShopList)
        floatingActionButtonAddShopItem = findViewById(R.id.floatingActionButtonAddShopItem)
    }

    private fun showShopList(shopItems: List<ShopItem>) {
        linearLayoutShopList.removeAllViews()
        for (item in shopItems) {
            val layoutResId = if (item.enabled) {
                R.layout.note_item_enabled
            } else {
                R.layout.note_item_disabled
            }

            val view =
                LayoutInflater.from(this).inflate(
                    layoutResId,
                    linearLayoutShopList,
                    false
                )

            view.setOnLongClickListener {
                viewModel.editShopItem(item)
                true
            }

            val textViewShopItemName = view.findViewById<TextView>(R.id.textViewShopItemName)
            val textViewShopItemCount = view.findViewById<TextView>(R.id.textViewShopItemCount)

            textViewShopItemName.text = item.name
            textViewShopItemCount.text = item.count.toString()

            linearLayoutShopList.addView(view)
        }
    }

    private fun setupRecyclerView() {
        val recyclerViewShopList = findViewById<RecyclerView>(R.id.recyclerViewShopList)
        adapter = ShopListAdapter()
        recyclerViewShopList.adapter = adapter
        adapter.setupPoolSize(recyclerViewShopList)
    }
}