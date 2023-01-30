package com.voitov.todolist.domain

data class ShopItem(
    val name: String,
    val count: Double,
    val priority: Priority,
    val enabled: Boolean = true,
    val id: Int = UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}