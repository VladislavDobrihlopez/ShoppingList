package com.voitov.todolist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.voitov.todolist.R
import com.voitov.todolist.databinding.ActivityShopItemBinding
import com.voitov.todolist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), ShopItemInfoFragment.OnFinishedListener {
    private lateinit var binding: ActivityShopItemBinding
    private lateinit var screenMode: String
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        parseIntent(intent)

        if (savedInstanceState === null) {
            launchAppropriateMode()
        }
    }

    private fun launchAppropriateMode() {
        val fragment = when (screenMode) {
            MODE_ADDING -> ShopItemInfoFragment.newInstanceOfFragmentInAddingMode()
            MODE_EDITING -> ShopItemInfoFragment.newInstanceOfFragmentInEditingMode(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewShopItem, fragment)
            .commit()
    }

    private fun parseIntent(intent: Intent) {
        if (!intent.hasExtra(EXTRA_MODE)) {
            throw RuntimeException("Param $EXTRA_MODE is absent")
        }

        val mode = intent.getStringExtra(EXTRA_MODE)

        if (mode != MODE_ADDING && mode != MODE_EDITING) {
            throw RuntimeException("Unknown screen mode of $mode")
        }
        screenMode = mode

        if (screenMode == MODE_EDITING) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw java.lang.RuntimeException("Param $EXTRA_ITEM_ID is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        fun newIntentModeAddingItem(context: Context): Intent {
            return Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_MODE, MODE_ADDING)
            }
        }

        fun newIntentModeEditingItem(context: Context, shopItemId: Int): Intent {
            return Intent(context, ShopItemActivity::class.java).apply {
                putExtra(EXTRA_MODE, MODE_EDITING)
                putExtra(EXTRA_ITEM_ID, shopItemId)
            }
        }

        private const val TAG = "ShopItemActivity"
        private const val EXTRA_MODE = "mode"
        private const val MODE_ADDING = "mode_adding"
        private const val MODE_EDITING = "mode_editing"
        private const val EXTRA_ITEM_ID = "item_id"
    }

    override fun onFinished() {
        finish()
    }
}