package com.voitov.todolist.domain

class GetShopItemUseCase(
    private val repository: ShopListRepository
) {
    suspend fun getShopItem(shopItemId: Int): ShopItem {
        return repository.getShopItem(shopItemId)
    }
}