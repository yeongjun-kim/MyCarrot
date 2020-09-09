package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityWriteBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.WriteViewModel

class WriteActivity : AppCompatActivity() {

    lateinit var binding: ActivityWriteBinding
    lateinit var writeViewModel: WriteViewModel
    val PICK_PROFILE_FROM_ALBUM = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        writeViewModel = ViewModelProvider(
            this, WriteViewModel.Factory(application)
        ).get(WriteViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        binding.apply {
            av = this@WriteActivity
            vm = writeViewModel
        }

        writeViewModel.getisWirteSuccess().observe(this){
            if(it) finish()
        }


    }

    fun getAlbum() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PROFILE_FROM_ALBUM && data != null) {
            val clipData = data.clipData!!
            val a = clipData.getItemAt(0)
            for (i in 0 until clipData.itemCount) {
                writeViewModel.addUriToList(clipData.getItemAt(i).uri)
            }
        }
    }

    override fun finish() {
        super.finish()
    }

    fun commit() {
        Log.d("fhrm", "WriteActivity -commit(),    : ${writeViewModel.uriImageList}")
        writeViewModel.commitItemObject()
//        writeViewModel.test()
    }
}
