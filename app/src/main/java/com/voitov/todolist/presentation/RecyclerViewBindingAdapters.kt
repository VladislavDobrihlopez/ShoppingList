package com.voitov.todolist.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.voitov.todolist.domain.Priority

@BindingAdapter("priorityColor")
fun bindPriorityColor(textView: TextView, priority: Priority) {
    textView.setBackgroundColor(
        ContextCompat.getColor(
            textView.context,
            when (priority) {
                Priority.LOW -> android.R.color.holo_green_light
                Priority.MEDIUM -> android.R.color.holo_orange_light
                Priority.HIGH -> android.R.color.holo_red_light
                else -> throw RuntimeException("Incorrect priority in onBindViewHolder")
            }
        )
    )
}