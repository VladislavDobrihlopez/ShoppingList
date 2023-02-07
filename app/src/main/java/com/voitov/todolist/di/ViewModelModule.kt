package com.voitov.todolist.di

import androidx.lifecycle.ViewModel
import com.voitov.todolist.presentation.MainActivityViewModel
import com.voitov.todolist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    @Binds
    fun bindShopItemViewModel(viewModel: ShopItemViewModel): ViewModel
}