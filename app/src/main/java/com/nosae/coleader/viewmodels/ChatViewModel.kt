package com.nosae.coleader.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Create by NOSAE on 2020/5/8
 */
class ChatViewModel: BaseViewModel() {



    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatViewModel() as T
        }
    }
}