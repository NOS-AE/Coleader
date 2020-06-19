package com.nosae.coleader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.base.MarkdownInterface
import com.nosae.coleader.databinding.ActivityMarkdownBinding
import com.nosae.coleader.utils.startActivityForResult
import com.nosae.coleader.utils.toast
import java.io.File
import java.io.PrintWriter

class MarkdownActivity : BaseActivity<ActivityMarkdownBinding>() {

    private lateinit var fraList: List<Fragment>

    override fun getLayoutId(): Int {
        return R.layout.activity_markdown
    }

    override fun initViews(b: ActivityMarkdownBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        fraList = listOf(
            MarkdownEditFragment.newInstance(),
            MarkdownPreviewFragment()
        )
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

    override fun getMenuId(): Int? {
        return R.menu.menu_markdown
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_save -> {
                val text = (fraList[0] as MarkdownInterface).getText()
                if (text.isNullOrBlank()) {
                    toast("内容为空")
                    return true
                }
                val file = File(getExternalFilesDir("md")!!.absolutePath, "md${System.currentTimeMillis()}.md")
                val out = PrintWriter(file)
                try {
                    out.println(text)
                    out.flush()
                    toast("保存成功")
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("md_path", file.absolutePath)
                    })
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast("保存失败")
                } finally {
                    try {
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toast("保存失败")
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    companion object {
        fun start(ctx: Activity) {
            ctx.startActivityForResult<MarkdownActivity>(requestCode = REQUEST_MD)
        }
        const val REQUEST_MD = 1
    }

}
