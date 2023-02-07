package com.voitov.todolist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.voitov.todolist.R
import com.voitov.todolist.databinding.ActivityMainBinding
import com.voitov.todolist.domain.ShopItem
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemInfoFragment.OnFinishedListener {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ShopListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainActivityViewModel

    private val isPortraitMode: Boolean
        get() = binding.fragmentContainerViewShopItemAlbum == null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ShopListApp).component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
        setupRecyclerView()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.getShopList().observe(this, Observer {
            Log.d(TAG, it.toString())
            adapter.submitList(it)
        })
    }

    private fun launchAppropriateMode(screenMode: String, shopItemId: Int = ShopItem.UNDEFINED_ID) {
        val fragment = when (screenMode) {
            MODE_ADDING -> ShopItemInfoFragment.newInstanceOfFragmentInAddingMode()
            MODE_EDITING -> ShopItemInfoFragment.newInstanceOfFragmentInEditingMode(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }

        supportFragmentManager.apply {
            popBackStack()
            beginTransaction()
                .replace(R.id.fragmentContainerViewShopItemAlbum, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        val recyclerViewShopList = binding.recyclerViewShopList
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
            if (isPortraitMode) {
                startActivity(ShopItemActivity.newIntentModeEditingItem(this@MainActivity, it.id))
            } else {
                launchAppropriateMode(MODE_EDITING, it.id)
            }
        }
    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.editShopItem(it)
        }
    }

    private fun setupFAB() {
        binding.floatingActionButtonAddShopItem.setOnClickListener {
            if (isPortraitMode) {
                startActivity(ShopItemActivity.newIntentModeAddingItem(this@MainActivity))
            } else {
                launchAppropriateMode(MODE_ADDING)
            }
        }
    }

    companion object {
        private const val TAG = "ShopItemActivity"
        private const val EXTRA_MODE = "mode"
        private const val MODE_ADDING = "mode_adding"
        private const val MODE_EDITING = "mode_editing"
        private const val EXTRA_ITEM_ID = "item_id"
    }

    override fun onFinished() {
        onBackPressed()
    }
}