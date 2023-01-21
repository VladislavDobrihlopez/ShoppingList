package com.voitov.todolist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.voitov.todolist.R
import com.voitov.todolist.domain.Priority
import com.voitov.todolist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var textInputLayoutShopItemName: TextInputLayout
    private lateinit var textViewShopItemName: TextView
    private lateinit var textInputLayoutShopItemCount: TextInputLayout
    private lateinit var textViewShopItemCount: TextView
    private lateinit var radioButtonLow: RadioButton
    private lateinit var radioButtonMedium: RadioButton
    private lateinit var radioButtonHigh: RadioButton
    private lateinit var buttonSaveShopItem: Button

    private lateinit var screenMode: String
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent(intent)
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews()
        setupListeners()

        when (screenMode) {
            MODE_ADDING -> launchItemAddingMode()
            MODE_EDITING -> launchItemEditingMode()
        }
    }

    private fun launchItemEditingMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItemLD.observe(this, Observer {
            textViewShopItemName.text = it.name
            textViewShopItemCount.text = it.count.toString()
        })

        buttonSaveShopItem.setOnClickListener {
            val name = textViewShopItemName.text.toString()
            val count = textViewShopItemCount.text.toString()
            val priority = if (radioButtonLow.isChecked) {
                Priority.LOW
            } else if (radioButtonMedium.isChecked) {
                Priority.MEDIUM
            } else {
                Priority.HIGH
            }
            viewModel.editShopItem(name, count, priority)
        }
    }

    private fun launchItemAddingMode() {
        buttonSaveShopItem.setOnClickListener {
            val name = textViewShopItemName.text.toString()
            val count = textViewShopItemCount.text.toString()
            val priority = if (radioButtonLow.isChecked) {
                Priority.LOW
            } else if (radioButtonMedium.isChecked) {
                Priority.MEDIUM
            } else {
                Priority.HIGH
            }
            viewModel.addShopItem(name, count, priority)
        }
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

    private fun setupListeners() {
        viewModel.errorInputName.observe(this, Observer {
            val message = if (it) {
                getString(R.string.error_invalid_name)
            } else {
                null
            }
            textInputLayoutShopItemName.error = message
        })

        viewModel.errorInputCount.observe(this, Observer {
            val message = if (it) {
                getString(R.string.error_invalid_count)
            } else {
                null
            }
            textInputLayoutShopItemCount.error = message
        })

        textViewShopItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        textViewShopItemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.shallCloseScreen.observe(this, Observer {
            finish()
        })
    }

    private fun initViews() {
        textInputLayoutShopItemName = findViewById(R.id.textInputLayoutShopItemName)
        textViewShopItemName = findViewById(R.id.textViewShopItemName)
        textInputLayoutShopItemCount = findViewById(R.id.textInputLayoutShopItemCount)
        textViewShopItemCount = findViewById(R.id.textViewShopItemCount)
        radioButtonLow = findViewById(R.id.radioButtonLow)
        radioButtonMedium = findViewById(R.id.radioButtonMedium)
        radioButtonHigh = findViewById(R.id.radioButtonHigh)
        buttonSaveShopItem = findViewById(R.id.buttonSaveShopItem)
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
}