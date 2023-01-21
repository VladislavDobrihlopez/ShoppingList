package com.voitov.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.voitov.todolist.domain.Priority
import com.voitov.todolist.domain.ShopItem
import com.voitov.todolist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {
    private val shopItems =
        sortedSetOf<ShopItem>({ shopItem1, shopItem2 -> shopItem1.id.compareTo(shopItem2.id) })
    private val shopItemsMutable = MutableLiveData<List<ShopItem>>()
    private var autoIncrementedId = 0

    init {
        for (i in 1..10) {
            val enabled = Random.nextBoolean()
            val priority = when (Random.nextInt(3)) {
                0 -> Priority.LOW
                1 -> Priority.MEDIUM
                else -> Priority.HIGH
            }
            addShopItem(ShopItem("block of flats $i", 1.0, priority, enabled))
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
        shopItems.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopItems.find { it.id == shopItemId }
            ?: throw NullPointerException("There is no shopItem with id = $shopItems")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemsMutable
    }

    override fun removeShopItem(shopItem: ShopItem) {
        shopItems.remove(shopItem)
        notifyObservers()
    }

    private fun notifyObservers() {
        shopItemsMutable.value = shopItems.toList()
    }
}