package com.voitov.todolist.domain

import javax.inject.Inject

class GetShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {
    suspend fun getShopItem(shopItemId: Int): ShopItem {
        return repository.getShopItem(shopItemId)
    }
}