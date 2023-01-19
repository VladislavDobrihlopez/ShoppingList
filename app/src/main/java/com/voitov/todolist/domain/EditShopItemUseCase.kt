package com.voitov.todolist.domain

class EditShopItemUseCase(
    private val repository: ShopListRepository
) {
    fun editShopItem(shopItem: ShopItem) {
        repository.editShopItem(shopItem)
    }
}