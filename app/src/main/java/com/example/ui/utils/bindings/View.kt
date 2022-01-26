package com.example.ui.utils.bindings

import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisibility(view: View, shouldShow: Boolean) {
    view.visibility = if (shouldShow) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("toast")
fun showToastMessage(view: View, msg: String) {
    if (msg.isNotEmpty()) {
        Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
    }
}