package com.voitov.todolist.domain

class AddShopItemUseCase(
    private val repository: ShopListRepository
) {
    fun addShopItem(shopItem: ShopItem) {
        repository.addShopItem(shopItem)
    }
}