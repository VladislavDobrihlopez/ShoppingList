package com.voitov.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.todolist.domain.EditShopItemUseCase
import com.voitov.todolist.domain.GetShopListUseCase
import com.voitov.todolist.domain.RemoveShopItemUseCase
import com.voitov.todolist.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val removeShopItemUseCase: RemoveShopItemUseCase,
) : ViewModel() {

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