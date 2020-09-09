package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.WriteVpAdapter
import com.mvvm.mycarrot.databinding.ActivityWriteBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.WriteViewModel
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {

    lateinit var binding: ActivityWriteBinding
    lateinit var writeViewModel: WriteViewModel
    private val PICK_PROFILE_FROM_ALBUM = 10
    var adapter = WriteVpAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        writeViewModel = ViewModelProvider(
            this, WriteViewModel.Factory(application)
        ).get(WriteViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        binding.apply {
            av = this@WriteActivity
            vm = writeViewModel
            lifecycleOwner = this@WriteActivity
        }

        writeViewModel.getisWirteSuccess().observe(this) {
            if (it) finish()
        }


        writeViewModel.getUriList().observe(this) {
            Log.d("fhrm", "change")
            adapter.setList(it)
        }

        initVp()

        test3.setOnClickListener {
            Log.d(
                "fhrm",
                "WriteActivity -onCreate(),    : ${writeViewModel.uriImageList.value!!.size}"
            )
        }


    }

    private fun initVp() {
        binding.writeVp.adapter = adapter
        binding.writeVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.writeVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                writeViewModel.viewPagerPosition.value = position + 1
            }
        })
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
            for (i in 0 until clipData.itemCount) {
                writeViewModel.addUriToList(clipData.getItemAt(i).uri)
            }
            writeViewModel.uriImageList.value = writeViewModel.uriImageList.value
        }
    }
}