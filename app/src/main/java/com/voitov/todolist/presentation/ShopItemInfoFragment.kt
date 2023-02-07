package com.voitov.todolist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.voitov.todolist.databinding.FragmentShopItemInfoBinding
import com.voitov.todolist.domain.Priority
import com.voitov.todolist.domain.ShopItem
import javax.inject.Inject

class ShopItemInfoFragment : Fragment() {
    private var _binding: FragmentShopItemInfoBinding? = null
    private val binding: FragmentShopItemInfoBinding
        get() = _binding ?: throw RuntimeException("ShopItemInfoFragment == null")

    @Inject
    lateinit var viewModel: ShopItemViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private lateinit var onFinishedListener: OnFinishedListener

    override fun onAttach(context: Context) {
        (requireActivity().application as ShopListApp).component.inject(this)
        super.onAttach(context)

        if (context is OnFinishedListener) {
            onFinishedListener = context
        } else {
            throw RuntimeException("It seems like activity doesn't implement ${onFinishedListener.javaClass}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShopItemViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupListeners()
        launchAppropriateMode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
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
        with(binding) {
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
                this@ShopItemInfoFragment.viewModel.editShopItem(name, count, priority)
            }
        }
    }

    private fun launchItemAddingMode() {
        with(binding) {
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
                this@ShopItemInfoFragment.viewModel.addShopItem(name, count, priority)
            }
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
                throw RuntimeException("Param $KEY_ITEM_ID is absent")
            }
            shopItemId = args.getInt(KEY_ITEM_ID)
        }
    }

    private fun setupListeners() {
        binding.textViewShopItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.textViewShopItemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.shallCloseScreen.observe(viewLifecycleOwner, Observer {
            onFinishedListener.onFinished()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    interface OnFinishedListener {
        fun onFinished()
    }
}