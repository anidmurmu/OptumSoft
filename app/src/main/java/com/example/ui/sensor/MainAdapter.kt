package com.example.ui.sensor

import androidx.databinding.ViewDataBinding
import com.example.domain.model.sensor.SensorUiModel
import com.example.optumsoft.BR
import com.example.optumsoft.R
import com.example.ui.utils.base.ViewOnClickListener
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel
import com.example.ui.utils.base.recyclerview.BaseBindingViewHolder
import com.example.ui.utils.base.recyclerview.BaseViewHolderBindingFactory

/**
 * This class contains a factory method
 *
 */
class SensorVHFactory : BaseViewHolderBindingFactory() {

    /**
     * Factory method to create corresponding ViewHolder for item view in a Recyclerview
     *
     * @param binding binding for the view
     * @param viewType view id
     * @param viewClickCallback callback for click in item view
     * @return View holder for item view
     */
    override fun create(
        binding: ViewDataBinding,
        viewType: Int,
        viewClickCallback: ViewOnClickListener?
    ): BaseBindingViewHolder<out BaseBindingRVModel> {
        return when (viewType) {
            R.layout.item_sensor -> SensorViewHolder(binding, viewClickCallback)

            else -> BaseBindingViewHolder(binding)
        }
    }
}

/**
 *This is a view holder for item in a recyclerview
 *
 * @property viewClickCallback A callback for view click
 *
 * @param binding A binding for view
 */
class SensorViewHolder(
    binding: ViewDataBinding,
    private val viewClickCallback: ViewOnClickListener?
) : BaseBindingViewHolder<SensorRVModel>(binding) {
    override fun bindView(model: SensorRVModel) {
        itemView.setOnClickListener {
            viewClickCallback?.onViewClick(R.id.on_click_sensor_item, model.sensorUiModel)
        }
    }
}

/**
 * Wrapper class for item which contains model for it
 *
 * @property sensorUiModel Model for item view
 */
class SensorRVModel(val sensorUiModel: SensorUiModel) :
    BaseBindingRVModel {

    override fun getLayoutId(): Int {
        return R.layout.item_sensor
    }

    override fun getBindingPairs(): List<Pair<Int, Any>> {
        return listOf(Pair(BR.uiModel, sensorUiModel))
    }
}
