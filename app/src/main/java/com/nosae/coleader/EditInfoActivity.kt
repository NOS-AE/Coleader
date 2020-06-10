package com.nosae.coleader

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityEditInfoBinding
import com.nosae.coleader.utils.load
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.EditInfoViewModel

class EditInfoActivity : BaseActivity<ActivityEditInfoBinding>() {
    private val viewModel by viewModels<EditInfoViewModel> {
        EditInfoViewModel.Factory()
    }
    private val genderDialog by lazy {
        val data = arrayOf("保密", "男", "女")
        AlertDialog.Builder(this)
            .setTitle("选择性别")
            .setItems(data) { dialog, which ->
                dialog.dismiss()
                viewModel.userInfo.value?.gender = which
                viewModel.userInfo.value = viewModel.userInfo.value
            }.create()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_info
    }

    override fun initViews(b: ActivityEditInfoBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("修改资料")
        b.viewModel = viewModel
        viewModel.submitRes.observe(this) {
            toast(it ?: "修改成功")
        }
        b.ivAvatar.setOnClickListener {
            openGallery()
        }
        b.et3.setOnClickListener {
            genderDialog.show()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                mBinding.ivAvatar.load(it)
                viewModel.avatarUri = it
            }
        }
    }

}
