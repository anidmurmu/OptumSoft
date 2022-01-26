package com.example.ui.utils.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.utils.base.RVModelBindingAdapter
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel
import com.example.ui.utils.base.recyclerview.BaseRVBindingAdapter

@BindingAdapter("data")
fun <T : BaseBindingRVModel> setRecyclerViewProperties(recyclerView: RecyclerView, data: List<T>?) {
    data?.let {
        if (recyclerView.adapter is BaseRVBindingAdapter<*>) {
            (recyclerView.adapter as RVModelBindingAdapter).setItems(it)
        }
    }
}

@BindingAdapter("adapter")
fun <T : RVModelBindingAdapter> setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    recyclerView.adapter = adapter
}