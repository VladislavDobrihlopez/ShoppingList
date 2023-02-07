package com.voitov.todolist.di

import androidx.lifecycle.ViewModel
import com.voitov.todolist.presentation.MainActivityViewModel
import com.voitov.todolist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
interface ViewModelModule {
    @IntoMap
    @StringKey("MainActivityViewModel")
    @Binds
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @IntoMap
    @StringKey("ShopItemViewModel")
    @Binds
    fun bindShopItemViewModel(viewModel: ShopItemViewModel): ViewModel
}