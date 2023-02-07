package com.voitov.todolist.domain

import javax.inject.Inject

class RemoveShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {
    suspend fun removeShopItem(shopItem: ShopItem) {
        repository.removeShopItem(shopItem)
    }
}