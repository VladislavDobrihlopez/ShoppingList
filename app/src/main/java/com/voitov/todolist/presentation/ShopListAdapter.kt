package com.voitov.todolist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.voitov.todolist.R
import com.voitov.todolist.databinding.ShopItemDisabledBinding
import com.voitov.todolist.databinding.ShopItemEnabledBinding
import com.voitov.todolist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopListAdapter.ShopListViewHolder>(ShopItemDiffCallback()) {
    private var count = 0
    private var count2 = 0
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    fun setupPoolSize(recyclerView: RecyclerView) {
        with(recyclerView) {
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        Log.d(TAG, "OnCreateViewHolder, count = ${++count}")

        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Unknown viewType = $viewType")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        Log.d(TAG, "OnBindViewHolder, count = ${count2++}")
        val shopItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ShopItemEnabledBinding -> {
                binding.shopItem = shopItem
            }
            is ShopItemDisabledBinding -> {
                binding.shopItem = shopItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    class ShopListViewHolder(
        val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
    }

    companion object {
        private const val TAG = "ShopListAdapter"
        private const val VIEW_TYPE_ENABLED = 100
        private const val VIEW_TYPE_DISABLED = 101
        private const val MAX_POOL_SIZE = 10
    }
}