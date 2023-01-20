package com.voitov.todolist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.voitov.todolist.R
import com.voitov.todolist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {
    private var count = 0
    private var count2 = 0
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null
    var shopItems = listOf<ShopItem>()
        set(value) {
            val diffUtilCallback = ShopListDiffCallback(shopItems, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    fun setupPoolSize(recyclerView: RecyclerView) {
        with(recyclerView) {
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        Log.d(TAG, "OnCreateViewHolder, count = ${++count}")

        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.note_item_enabled
            else -> R.layout.note_item_disabled
        }

        return ShopListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    layout,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        Log.d(TAG, "OnBindViewHolder, count = ${count2++}")
        val shopItem = shopItems[position]

        with(holder) {
            textViewShopItemName.text = shopItem.name
            textViewShopItemCount.text = shopItem.count.toString()
            itemView.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(shopItem)
                true
            }
            itemView.setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopItems[position].enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    override fun getItemCount(): Int {
        return shopItems.size
    }

    class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewShopItemName: TextView = itemView.findViewById(R.id.textViewShopItemName)
        val textViewShopItemCount: TextView = itemView.findViewById(R.id.textViewShopItemCount)
    }

    companion object {
        private const val TAG = "ShopListAdapter"
        private const val VIEW_TYPE_ENABLED = 100
        private const val VIEW_TYPE_DISABLED = 101
        private const val MAX_POOL_SIZE = 10
    }

    interface OnShopItemLongClickListener {
        fun onLongClick(shopItem: ShopItem)
    }
}