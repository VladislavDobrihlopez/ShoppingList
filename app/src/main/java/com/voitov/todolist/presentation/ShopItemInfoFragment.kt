package com.voitov.todolist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.voitov.todolist.R
import com.voitov.todolist.domain.Priority
import com.voitov.todolist.domain.ShopItem

class ShopItemInfoFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var textInputLayoutShopItemName: TextInputLayout
    private lateinit var textViewShopItemName: TextView
    private lateinit var textInputLayoutShopItemCount: TextInputLayout
    private lateinit var textViewShopItemCount: TextView
    private lateinit var radioButtonLow: RadioButton
    private lateinit var radioButtonMedium: RadioButton
    private lateinit var radioButtonHigh: RadioButton
    private lateinit var buttonSaveShopItem: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews(view)
        setupListeners()
        launchAppropriateMode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    private fun launchAppropriateMode() {
        when (screenMode) {
            MODE_ADDING -> launchItemAddingMode()
            MODE_EDITING -> launchItemEditingMode()
        }
    }

    private fun launchItemEditingMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItemLD.observe(viewLifecycleOwner, Observer {
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

    private fun parseArguments() {
        val args = requireArguments()

        if (!args.containsKey(KEY_MODE)) {
            throw RuntimeException("Param $KEY_MODE is absent")
        }

        val mode = args.getString(KEY_MODE)

        if (mode != MODE_ADDING && mode != MODE_EDITING) {
            throw RuntimeException("Unknown screen mode of $mode")
        }
        screenMode = mode

        if (screenMode == MODE_EDITING) {
            if (!args.containsKey(KEY_ITEM_ID)) {
                throw java.lang.RuntimeException("Param $KEY_ITEM_ID is absent")
            }
            shopItemId = args.getInt(KEY_ITEM_ID)
        }
    }

    private fun setupListeners() {
        viewModel.errorInputName.observe(viewLifecycleOwner, Observer {
            val message = if (it) {
                getString(R.string.error_invalid_name)
            } else {
                null
            }
            textInputLayoutShopItemName.error = message
        })

        viewModel.errorInputCount.observe(viewLifecycleOwner, Observer {
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

        viewModel.shallCloseScreen.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })
    }

    private fun initViews(view: View) {
        textInputLayoutShopItemName = view.findViewById(R.id.textInputLayoutShopItemName)
        textViewShopItemName = view.findViewById(R.id.textViewShopItemName)
        textInputLayoutShopItemCount = view.findViewById(R.id.textInputLayoutShopItemCount)
        textViewShopItemCount = view.findViewById(R.id.textViewShopItemCount)
        radioButtonLow = view.findViewById(R.id.radioButtonLow)
        radioButtonMedium = view.findViewById(R.id.radioButtonMedium)
        radioButtonHigh = view.findViewById(R.id.radioButtonHigh)
        buttonSaveShopItem = view.findViewById(R.id.buttonSaveShopItem)
    }


    companion object {
        fun newInstanceOfFragmentInAddingMode(): ShopItemInfoFragment {
            return ShopItemInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_ADDING)
                }
            }
        }

        fun newInstanceOfFragmentInEditingMode(shopItemId: Int): ShopItemInfoFragment {
            return ShopItemInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MODE, MODE_EDITING)
                    putInt(KEY_ITEM_ID, shopItemId)
                }
            }
        }

        private const val TAG = "ShopItemInfoFragment"
        private const val KEY_MODE = "mode"
        private const val MODE_UNKNOWN = "mode_unknown"
        private const val MODE_ADDING = "mode_adding"
        private const val MODE_EDITING = "mode_editing"
        private const val KEY_ITEM_ID = "item_id"
    }
}