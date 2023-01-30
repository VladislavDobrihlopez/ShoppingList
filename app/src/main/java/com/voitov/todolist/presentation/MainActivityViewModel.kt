package com.voitov.todolist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.todolist.data.ShopListRepositoryImpl
import com.voitov.todolist.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val shopListRepository: ShopListRepository = ShopListRepositoryImpl(application)
    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(shopListRepository)

    private val scope = CoroutineScope(Dispatchers.Main)

    fun getShopList(): LiveData<List<ShopItem>> {
        return getShopListUseCase.getShopList()
    }

    fun editShopItem(shopItem: ShopItem) {
        scope.launch {
            editShopItemUseCase.editShopItem(shopItem.copy(enabled = !shopItem.enabled))
        }
    }

    fun removeShopItem(shopItem: ShopItem) {
        scope.launch {
            removeShopItemUseCase.removeShopItem(shopItem)
        }
    }

    override fun onCleared() {
        scope.cancel()
    }
}