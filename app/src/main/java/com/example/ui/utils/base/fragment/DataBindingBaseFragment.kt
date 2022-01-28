package com.example.ui.utils.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ui.utils.base.viewmodel.BaseViewModel


/**
 *This is the base class for all the fragments
 * @param V is a viewbinding object ( usually autogenerated) belonging to the screen
 * @param VM is a viewmodel ( the new architecture is going to be MVVM) and the viewmodel class
 * that is passed needs to extend the androids lifecycle aware viewmode class
 *
 **/
abstract class DataBindingBaseFragment<in V : ViewDataBinding>() : Fragment() {

    private val baseViewModel: BaseViewModel by viewModels()

    companion object {
        const val NO_LAYOUT = 0
    }

    protected open val layoutResource = NO_LAYOUT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutResource == NO_LAYOUT) return null

        val binding = DataBindingUtil.inflate<V>(inflater, layoutResource, container, false)
        binding.lifecycleOwner = this
        onViewDataBindingCreated(binding)
        return binding.root
    }

    /**
     * This method to be overrider in every fragment, it is called in onCreateView()
     *
     * @param binding Base class for generated data binding classes.
     */
    protected abstract fun onViewDataBindingCreated(binding: V)
}