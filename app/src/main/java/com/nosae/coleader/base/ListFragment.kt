package com.nosae.coleader.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.R
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Create by NOSAE on 2020/5/7
 */
abstract class ListFragment<T: RecyclerView.Adapter<*>>: Fragment(R.layout.fragment_list) {

    protected abstract val adapter: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
    }
}