package com.voitov.todolist.di

import com.voitov.todolist.data.ShopListRepositoryImpl
import com.voitov.todolist.domain.ShopListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository
}