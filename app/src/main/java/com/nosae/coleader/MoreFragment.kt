package com.nosae.coleader

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nosae.coleader.base.BaseFragment
import com.nosae.coleader.base.BaseToolbarFragment
import com.nosae.coleader.data.TempData
import com.nosae.coleader.databinding.FragmentMoreBinding
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.viewmodels.MoreViewModel

/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : BaseToolbarFragment<FragmentMoreBinding>() {

    private val viewModel by viewModels<MoreViewModel> {
        MoreViewModel.Factory()
    }
    private val dialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("提示")
            .setMessage("确认退出登录")
            .setPositiveButton("确定") { _, _ ->
                logout()
            }
            .create()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_more
    }

    override fun initView(b: FragmentMoreBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        b.viewModel = this.viewModel
        b.ivAvatar.setColorFilter(0x33ffffff)
        b.fab.setOnClickListener {
            startActivity<EditInfoActivity>()
        }
        b.llLogout.setOnClickListener {
            dialog.show()
        }
    }

    private fun logout() {
        TempData.clear()
        SharedPref.loginAccount = ""
        SharedPref.userPassword = ""
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
