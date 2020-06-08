package com.nosae.coleader.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
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
    }

    @LayoutRes abstract fun getLayoutId(): Int

    abstract fun initView(b: T, savedInstanceState: Bundle?)

}