package com.voitov.todolist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "shopItems.db"
        private var instance: AppDatabase? = null
        private val lock = Any()
        fun getInstance(application: Application): AppDatabase {
            instance?.let {
                return it
            }

            synchronized(lock) {
                instance?.let {
                    return it
                }
            }
            val db = Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME).build()
            instance = db
            return db
        }
    }

    abstract fun getShopListDao(): ShopListDao
}