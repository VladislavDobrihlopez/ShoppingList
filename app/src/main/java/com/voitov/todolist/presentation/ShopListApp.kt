package com.voitov.todolist.presentation

import android.app.Application
import com.voitov.todolist.di.DaggerApplicationComponent

class ShopListApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}