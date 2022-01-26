package com.example.ui.utils.base.recyclerview

import androidx.databinding.ViewDataBinding
import com.example.ui.utils.base.ViewOnClickListener

abstract class BaseViewHolderBindingFactory {
    abstract fun create(
        binding: ViewDataBinding,
        viewType: Int,
        viewClickCallback: ViewOnClickListener?
    ): BaseBindingViewHolder<out BaseBindingRVModel>
}
