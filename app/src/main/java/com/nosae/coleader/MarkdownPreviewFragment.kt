package com.nosae.coleader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zzhoujay.markdown.MarkDown
import kotlinx.android.synthetic.main.fragment_markdown_preview.*

class MarkdownPreviewFragment : Fragment(R.layout.fragment_markdown_preview) {

    private var pendingString: String? = null

    fun onShow(text: String?) {
        if (tv_md == null) {
            pendingString = text
            return
        }
        if (text.isNullOrBlank()) {
            tv_md.text = "无可展示内容"
        } else {
            val spanned = MarkDown.fromMarkdown(text, { null }, tv_md)
            tv_md.text = spanned
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onShow(pendingString)
    }
}
