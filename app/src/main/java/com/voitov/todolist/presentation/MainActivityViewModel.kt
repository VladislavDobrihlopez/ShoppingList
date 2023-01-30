package com.voitov.todolist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.voitov.todolist.data.ShopListRepositoryImpl
import com.voitov.todolist.domain.*
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val shopListRepository: ShopListRepository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(shopListRepository)

    fun getShopList(): LiveData<List<ShopItem>> {
        return getShopListUseCase.getShopList()
    }

    fun editShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            editShopItemUseCase.editShopItem(shopItem.copy(enabled = !shopItem.enabled))
        }
    }

    fun removeShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            removeShopItemUseCase.removeShopItem(shopItem)
        }
    }
}