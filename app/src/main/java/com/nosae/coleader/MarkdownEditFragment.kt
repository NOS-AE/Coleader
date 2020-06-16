package com.nosae.coleader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.nosae.coleader.base.MarkdownInterface
import kotlinx.android.synthetic.main.fragment_markdown_edit.*

class MarkdownEditFragment : Fragment(R.layout.fragment_markdown_edit), MarkdownInterface,
    View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_add_l_header.setOnClickListener(this)
        btn_add_m_header.setOnClickListener(this)
        btn_add_s_header.setOnClickListener(this)
        btn_add_bold.setOnClickListener(this)
        btn_add_italic.setOnClickListener(this)
        btn_add_code.setOnClickListener(this)
        btn_add_link.setOnClickListener(this)
        btn_add_list.setOnClickListener(this)
        btn_add_mention.setOnClickListener(this)
        btn_add_quote.setOnClickListener(this)
    }

    override fun getText(): String? {
        return et.text.toString()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_add_l_header -> {
                addFormat("#")
            }
            R.id.btn_add_m_header -> {
                addFormat("##")
            }
            R.id.btn_add_s_header -> {
                addFormat("###")
            }
            R.id.btn_add_bold -> {
                addFormat("****", 2)
            }
            R.id.btn_add_italic -> {
                addFormat("__", 1)
            }
            R.id.btn_add_quote -> {
                addFormat(">")
            }
            R.id.btn_add_code -> {
                addFormat("``", 1)
            }
            R.id.btn_add_link -> {
                addFormat("[](url)", 1)
            }
            R.id.btn_add_list -> {
                addFormat("-")
            }
            R.id.btn_add_mention -> {
                addFormat("@", 1)
            }
        }
    }

    private fun addFormat(format: String, cursorPos: Int = -1) {
        val cursorIndex = et.selectionStart
        val text = et.text.toString()
        val preText = text.substring(0, cursorIndex)
        val sufText = text.substring(cursorIndex)
        val isPreBlank = preText.isNotEmpty()
                && preText[preText.lastIndex] != ' '
                && preText[preText.lastIndex] != '\n'
        var newText = preText
        if (isPreBlank)
            newText += " "
        newText += "$format $sufText"
        val newCursorIndex =
            cursorIndex + (if (isPreBlank) 1 else 0) + (if (cursorPos == -1) format.length + 1 else cursorPos)
        et.setText(newText)
        et.setSelection(newCursorIndex)
    }
}
