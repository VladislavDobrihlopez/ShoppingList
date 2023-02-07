package com.voitov.todolist.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(
    private val repository: ShopListRepository
) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return repository.getShopList()
    }
}