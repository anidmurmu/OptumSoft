package com.example.ui.utils.bindings

import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter

@BindingAdapter("backgroundColor")
fun setBitmapImage(cardView: CardView, color: String) {
    cardView.apply {
        setCardBackgroundColor(Color.parseColor(color))
    }
}