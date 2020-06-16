package com.nosae.coleader

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.base.MarkdownInterface
import com.nosae.coleader.databinding.ActivityMarkdownBinding

class MarkdownActivity : BaseActivity<ActivityMarkdownBinding>() {

    private val fraList = listOf(
        MarkdownEditFragment(),
        MarkdownPreviewFragment()
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_markdown
    }

    override fun initViews(b: ActivityMarkdownBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        b.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fraList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fraList[position]
            }
        }
        TabLayoutMediator(b.layoutTab, b.pager) { tab, pos ->
            tab.text = if (pos == 0) {
                "编辑"
            } else {
                "预览"
            }
        }.attach()
        b.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    val text = (fraList[0] as MarkdownInterface).getText()
                    (fraList[1] as MarkdownPreviewFragment).onShow(text)
                }
            }
        })
    }

}
