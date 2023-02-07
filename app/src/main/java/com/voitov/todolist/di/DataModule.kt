package com.voitov.todolist.di

import android.app.Application
import com.voitov.todolist.data.AppDatabase
import com.voitov.todolist.data.ShopListDao
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    companion object {
        @ApplicationScope
        @Provides
        fun provideDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).getShopListDao()
        }
    }
}