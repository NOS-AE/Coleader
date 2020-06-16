package com.nosae.coleader.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.nosae.coleader.R
import com.nosae.coleader.utils.Bus
import com.nosae.coleader.utils.registerBus
import com.nosae.coleader.utils.setStatusBarIcon
import com.nosae.coleader.utils.unregisterBus
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Create by NOSAE on 2020/4/22
 */
abstract class BaseActivity<T: ViewDataBinding>: AppCompatActivity() {
    protected lateinit var mBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarIcon(true)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        initViews(mBinding, savedInstanceState)
        if (this::class.java.isAnnotationPresent(Bus::class.java)) {
            registerBus(this)
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initViews(b: T, savedInstanceState: Bundle?)

    open fun bindToolbar(title: String) {
        setSupportActionBar(toolbar as Toolbar)
        this.title = title
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        (toolbar as Toolbar).setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    }

    open fun setCenterTitle(title: String) {
        val tv = toolbar_tv ?: return
        tv.text = title
    }

    @MenuRes
    open fun getMenuId(): Int? = null

    open fun onCreateMenu(menu: Menu) {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuId()?.also {
            menuInflater.inflate(it, menu)
        } ?: return super.onCreateOptionsMenu(menu)

        menu?.let { onCreateMenu(it) }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onDestroy() {
        if (this::class.java.isAnnotationPresent(Bus::class.java)) {
            unregisterBus(this)
        }
        super.onDestroy()
    }

    fun launch(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = lifecycleScope.launch(context, start, block)
}