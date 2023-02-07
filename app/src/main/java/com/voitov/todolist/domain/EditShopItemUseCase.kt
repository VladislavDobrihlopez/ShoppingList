package com.voitov.todolist.domain

import javax.inject.Inject

class EditShopItemUseCase @Inject constructor(
    private val repository: ShopListRepository
) {
    suspend fun editShopItem(shopItem: ShopItem) {
        repository.editShopItem(shopItem)
    }
}