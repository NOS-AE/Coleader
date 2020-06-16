package com.nosae.coleader

import androidx.fragment.app.Fragment
import com.zzhoujay.markdown.MarkDown
import kotlinx.android.synthetic.main.fragment_markdown_preview.*

class MarkdownPreviewFragment : Fragment(R.layout.fragment_markdown_preview) {

    fun onShow(text: String?) {
        if (text.isNullOrBlank()) {
            tv_md.text = "无可展示内容"
        } else {
            val spanned = MarkDown.fromMarkdown(text, { null }, tv_md)
            tv_md.text = spanned
        }
    }
}
