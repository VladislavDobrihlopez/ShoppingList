package com.voitov.todolist.data

import com.voitov.todolist.domain.ShopItem

class ShopListMapper {
    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            priority = shopItem.priority,
            enabled = shopItem.enabled
        )
    }

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            priority = shopItemDbModel.priority,
            enabled = shopItemDbModel.enabled
        )
    }

    fun mapDbModelsListToEntityList(shopItemDbModels: List<ShopItemDbModel>): List<ShopItem> {
        return shopItemDbModels.map { mapDbModelToEntity(it) }
    }
}