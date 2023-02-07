package com.voitov.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface ShopListDao {
    @Insert(onConflict = REPLACE)
    suspend fun addShopItem(shopItem: ShopItemDbModel)

    @Insert(onConflict = REPLACE)
    suspend fun editShopItemDbModel(shopItem: ShopItemDbModel)

    @Query("SELECT * FROM ShopItems WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItemDbModel(shopItemId: Int): ShopItemDbModel?

    @Query("SELECT * FROM ShopItems")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Delete
    suspend fun removeShopItemDbModel(shopItem: ShopItemDbModel)
}