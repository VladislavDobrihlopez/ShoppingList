package com.voitov.todolist.data

import android.database.Cursor
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
    fun addShopItemSync(shopItem: ShopItemDbModel)

    @Query("DELETE FROM ShopItems WHERE id=:id")
    fun removeShopItemDbModelSync(id: Int): Int

    @Insert(onConflict = REPLACE)
    fun editShopItemDbModelSync(shopItem: ShopItemDbModel)

    @Insert(onConflict = REPLACE)
    suspend fun editShopItemDbModel(shopItem: ShopItemDbModel)

    @Query("SELECT * FROM ShopItems WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItemDbModel(shopItemId: Int): ShopItemDbModel?

    @Query("SELECT * FROM ShopItems")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM ShopItems")
    fun getShopListCursor(): Cursor

    @Delete
    suspend fun removeShopItemDbModelSync(shopItem: ShopItemDbModel)
}