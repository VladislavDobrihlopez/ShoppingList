package com.voitov.todolist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.voitov.todolist.R

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
            adapter.submitList(it)
        })
    }

    private fun initViews() {
        linearLayoutShopList = findViewById(R.id.linearLayoutShopList)
        floatingActionButtonAddShopItem = findViewById(R.id.floatingActionButtonAddShopItem)
    }

    private fun setupRecyclerView() {
        val recyclerViewShopList = findViewById<RecyclerView>(R.id.recyclerViewShopList)
        adapter = ShopListAdapter()
        recyclerViewShopList.adapter = adapter
        adapter.setupPoolSize(recyclerViewShopList)
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(recyclerViewShopList)
        setupFAB()
    }

    private fun setupSwipeListener(recyclerViewShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItemPosition = viewHolder.adapterPosition
                val shopItem = adapter.currentList[shopItemPosition]
                viewModel.removeShopItem(shopItem)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerViewShopList)
    }

    private fun setupClickListener() {
        adapter.onShopItemClickListener = {
            Log.d(TAG, it.toString())
            startActivity(ShopItemActivity.newIntentModeEditingItem(this@MainActivity, it.id))
        }
    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.editShopItem(it)
        }
    }

    private fun setupFAB() {
        floatingActionButtonAddShopItem.setOnClickListener {
            startActivity(ShopItemActivity.newIntentModeAddingItem(this@MainActivity))
        }
    }
}