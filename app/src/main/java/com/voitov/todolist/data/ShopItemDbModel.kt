package com.voitov.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.voitov.todolist.domain.Priority

@Entity(tableName = "ShopItems")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Double,
    val priority: Priority,
    val enabled: Boolean = true,
)