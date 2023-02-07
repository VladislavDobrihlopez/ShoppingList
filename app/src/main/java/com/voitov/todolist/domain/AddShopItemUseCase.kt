package com.voitov.todolist.domain

import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {
    suspend fun addShopItem(shopItem: ShopItem) {
        repository.addShopItem(shopItem)
    }
}