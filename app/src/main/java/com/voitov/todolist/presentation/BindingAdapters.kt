package com.voitov.todolist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.voitov.todolist.R

@BindingAdapter("errorOnInvalidName")
fun bindErrorOnInvalidName(textInputLayout: TextInputLayout, isErrorPresent: Boolean) {
    val message = if (isErrorPresent) {
        textInputLayout.context.getString(R.string.error_invalid_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorOnInvalidCount")
fun bindErrorOnInvalidCount(textInputLayout: TextInputLayout, isErrorPresent: Boolean) {
    val message = if (isErrorPresent) {
        textInputLayout.context.getString(R.string.error_invalid_count)
    } else {
        null
    }
    textInputLayout.error = message
}