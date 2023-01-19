package com.voitov.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.voitov.todolist.domain.Priority
import com.voitov.todolist.domain.ShopItem
import com.voitov.todolist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {
    private val shopItems = mutableListOf<ShopItem>()
    private val shopItemsMutable = MutableLiveData<List<ShopItem>>()
    private var autoIncrementedId = 0

    init {
        for (i in 1..10) {
            addShopItem(ShopItem("block of flats $i", 1.0, Priority.HIGH, true))
        }
        notifyObservers()
    }

    override fun addShopItem(shopItem: ShopItem) {
        var shopItemToBeAdded = shopItem
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItemToBeAdded = shopItem.copy(id = autoIncrementedId++)
        }
        shopItems.add(shopItemToBeAdded)
        notifyObservers()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        removeShopItem(oldShopItem.id)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopItems.find { it.id == shopItemId }
            ?: throw NullPointerException("There is no shopItem with id = $shopItems")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemsMutable
    }

    override fun removeShopItem(shopItemId: Int) {
        shopItems.removeAt(shopItemId)
        notifyObservers()
    }

    private fun notifyObservers() {
        shopItemsMutable.value = shopItems.toList()
    }
}