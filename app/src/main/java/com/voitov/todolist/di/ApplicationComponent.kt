package com.voitov.todolist.di

import android.app.Application
import com.voitov.todolist.presentation.MainActivity
import com.voitov.todolist.presentation.ShopItemInfoFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemInfoFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): ApplicationComponent
    }
}