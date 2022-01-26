package com.example.ui.utils.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Base adapter that wraps the generic recycler view adapter logic and expose only required component logic
 */
abstract class BaseRVBindingAdapter<T : BaseBindingRVModel>(var listItems: List<T>) :
    RecyclerView.Adapter<BaseBindingViewHolder<T>>() {

    fun setItems(listItems: List<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<T> {
        return getViewHolder(parent.getBinding(viewType), viewType)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<T>, position: Int) {
        holder.bindView(listItems[position])
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.onBindWithPayload(listItems[position], payloads)
    }

    override fun getItemViewType(position: Int): Int = getLayoutId(position, listItems[position])

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(binding: ViewDataBinding, viewType: Int): BaseBindingViewHolder<T>

    override fun getItemCount(): Int = listItems.size
}

fun ViewGroup.getBinding(viewType: Int): ViewDataBinding {
    val layoutInflater = LayoutInflater.from(this.context)
    return DataBindingUtil.inflate(
        layoutInflater, viewType, this, false
    )
}
