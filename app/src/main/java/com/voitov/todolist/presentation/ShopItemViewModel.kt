package com.voitov.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voitov.todolist.data.ShopListRepositoryImpl
import com.voitov.todolist.domain.*

class ShopItemViewModel : ViewModel() {
    private val shopListRepository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(shopListRepository)
    private val addShopItemUseCase = AddShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)

    private val _errorInputName = MutableLiveData<Boolean>(false)
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>(false)
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItemLD = MutableLiveData<ShopItem>()
    val shopItemLD: LiveData<ShopItem>
        get() = _shopItemLD

    private val _shallCloseScreen = MutableLiveData<Unit>()
    val shallCloseScreen: LiveData<Unit>
        get() = _shallCloseScreen

    fun addShopItem(inputName: String?, inputCount: String?, priority: Priority) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (areFieldsValid(name, count)) {
            val newShopItem = ShopItem(name, count, priority)
            addShopItemUseCase.addShopItem(newShopItem)
            closeScreen()
        }
    }

    fun editShopItem(
        inputName: String?,
        inputCount: String?,
        priority: Priority,
    ) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (areFieldsValid(name, count)) {
            _shopItemLD.value?.let {
                val oldShopItem = it
                val editedShopItem = oldShopItem.copy(
                    name = name,
                    count = count,
                    priority = priority
                )
                editShopItemUseCase.editShopItem(editedShopItem)
                closeScreen()
            }
        }
    }

    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItem(shopItemId)
        _shopItemLD.value = shopItem
    }

    private fun closeScreen() {
        _shallCloseScreen.value = Unit
    }

    private fun parseName(inputName: String?): String {
        return inputName ?: ""
    }

    private fun parseCount(inputCount: String?): Double {
        return try {
            inputCount?.trim()?.toDouble() ?: DEFAULT_COUNT
        } catch (ex: Exception) {
            DEFAULT_COUNT
        }
    }

    private fun areFieldsValid(name: String, count: Double): Boolean {
        var isValid = true

        if (name.isBlank()) {
            isValid = false
            _errorInputName.value = true
        }

        if (count <= MIN_ALLOWED_COUNT_VALUE || count > MAX_ALLOWED_COUNT_VALUE) {
            isValid = false
            _errorInputCount.value = true
        }

        return isValid
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    companion object {
        private const val TAG = "ShopItemViewModel"
        private const val DEFAULT_COUNT = 0.0
        private const val MAX_ALLOWED_COUNT_VALUE = 9999
        private const val MIN_ALLOWED_COUNT_VALUE = 0
    }
}