package com.nosae.coleader.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nosae.coleader.utils.Bus
import com.nosae.coleader.utils.registerBus
import com.nosae.coleader.utils.unregisterBus

/**
 * Create by NOSAE on 2020/4/22
 */
abstract class BaseFragment<T: ViewDataBinding>: Fragment() {

    protected lateinit var mBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(mBinding, savedInstanceState)
        if (this::class.java.isAnnotationPresent(Bus::class.java)) {
            registerBus(this)
        }
    }

    override fun onDestroyView() {
        if (this::class.java.isAnnotationPresent(Bus::class.java)) {
            unregisterBus(this)
        }
        super.onDestroyView()
    }

    @LayoutRes abstract fun getLayoutId(): Int

    abstract fun initView(b: T, savedInstanceState: Bundle?)

}