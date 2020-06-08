package com.nosae.coleader.adapters

import android.content.Context
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseAdapter
import com.nosae.coleader.databinding.ItemListArticleBinding

/**
 * Create by NOSAE on 2020/4/24
 */
class ArticleAdapter(ctx: Context): BaseAdapter<Article, ItemListArticleBinding>(ctx) {

    override val diffCallback: BaseDiffUtilCallback = DiffCallback()

    override fun getLayoutId(): Int {
        return R.layout.item_list_article
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.mBinding.artical = mList[position]
    }

    inner class DiffCallback: BaseDiffUtilCallback(listOf(), listOf()) {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

data class Article(
    var id: Int,
    var title: String,
    var content: String,
    var imgUrl: String
)