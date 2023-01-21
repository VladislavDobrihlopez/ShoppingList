package com.voitov.todolist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.voitov.todolist.R
import com.voitov.todolist.domain.Priority
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
            else -> R.layout.shop_item_disabled
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
        val shopItem = getItem(position)

        with(holder) {
            textViewShopItemName.text = shopItem.name
            textViewShopItemCount.text = shopItem.count.toString()

            textViewPriority.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    when (shopItem.priority) {
                        Priority.LOW -> android.R.color.holo_green_light
                        Priority.MEDIUM -> android.R.color.holo_orange_light
                        Priority.HIGH -> android.R.color.holo_red_light
                        else -> throw RuntimeException("Incorrect priority in onBindViewHolder")
                    }
                )
            )

            if (!shopItem.enabled) {
                textViewPriority.alpha = ALPHA_FOR_VIEW_TYPE_DISABLED
            }

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
        return if (getItem(position).enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewShopItemName: TextView = itemView.findViewById(R.id.textViewShopItemName)
        val textViewShopItemCount: TextView = itemView.findViewById(R.id.textViewShopItemCount)
        val textViewPriority: TextView = itemView.findViewById(R.id.textViewPriority)
    }

    companion object {
        private const val TAG = "ShopListAdapter"
        private const val VIEW_TYPE_ENABLED = 100
        private const val VIEW_TYPE_DISABLED = 101
        private const val ALPHA_FOR_VIEW_TYPE_DISABLED = 0.5F
        private const val MAX_POOL_SIZE = 10
    }
}