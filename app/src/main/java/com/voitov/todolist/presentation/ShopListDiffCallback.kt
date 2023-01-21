package com.voitov.todolist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.voitov.todolist.domain.ShopItem

class ShopListDiffCallback(
    private val oldItems: List<ShopItem>,
    private val newItems: List<ShopItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldShopItem = oldItems[oldItemPosition]
        val newShopItem = newItems[newItemPosition]
        return oldShopItem.id == newShopItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldShopItem = oldItems[oldItemPosition]
        val newShopItem = newItems[newItemPosition]
        return oldShopItem == newShopItem
    }
}