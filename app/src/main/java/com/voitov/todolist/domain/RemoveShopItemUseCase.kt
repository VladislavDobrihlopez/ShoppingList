package com.voitov.todolist.domain

class RemoveShopItemUseCase(
    private val repository: ShopListRepository
) {
    fun removeShopItem(shopItemId: Int) {
        repository.removeShopItem(shopItemId)
    }
}