package com.example.ui.utils.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseBindingViewHolder<T : BaseBindingRVModel>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    open fun bindView(model: T) {
    }

    open fun onBindWithPayload(model: T, payloads: List<Any>) {
        if (isAutoBind()) {
            (model as BaseBindingRVModel).let {
                for (pair in it.getBindingPairs()) {
                    binding.setVariable(pair.first, pair.second)
                }
            }
        }
        bindView(model)
    }

    open fun isAutoBind() = true
}
