package com.example.ui.utils.bindings

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:imageBitmap")
fun setBitmapImage(imageView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imageView.setImageBitmap(it)
    }
}

@BindingAdapter("imgDrawable")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    drawable?.let {
        view.setImageDrawable(it)
    }
}

@BindingAdapter("imgRes")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}