package com.voitov.todolist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.voitov.todolist.domain.ShopItem
import com.voitov.todolist.domain.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val dao: ShopListDao,
    private val mapper: ShopListMapper,
    private val application: Application,
) : ShopListRepository {
    override suspend fun addShopItem(shopItem: ShopItem) {
        dao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        dao.editShopItemDbModel(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = dao.getShopItemDbModel(shopItemId)
        dbModel?.let {
            return mapper.mapDbModelToEntity(it)
        }
        throw NullPointerException("There is no shopItem with id = $shopItemId")
    }

    override fun getShopList(): LiveData<List<ShopItem>> =
        Transformations.map(dao.getShopList()) {
            mapper.mapDbModelsListToEntityList(it)
        }


    override suspend fun removeShopItem(shopItem: ShopItem) {
        dao.removeShopItemDbModel(mapper.mapEntityToDbModel(shopItem))
    }
}