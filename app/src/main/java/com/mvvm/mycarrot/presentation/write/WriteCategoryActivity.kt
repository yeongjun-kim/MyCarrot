package com.mvvm.mycarrot.presentation.write

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R

class WriteCategoryActivity : AppCompatActivity() {

    private lateinit var binding: com.mvvm.mycarrot.databinding.ActivityCategoryBinding
    private lateinit var writeViewModel: WriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initBinding()
        initSelectedCategory()
        initStatusBar()

    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.apply {
            av = this@WriteCategoryActivity
            lifecycleOwner = this@WriteCategoryActivity
        }
    }

    private fun initViewModel() {
        writeViewModel = ViewModelProvider(
            this, WriteViewModel.Factory(application)
        ).get(WriteViewModel::class.java)
    }

    private fun initSelectedCategory() {
        var selectedCategory = writeViewModel.getcategory().value!!

        when (selectedCategory) {
            binding.categoryTvDigital.text -> binding.categoryTvDigital.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvFurniture.text -> binding.categoryTvFurniture.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvLife.text -> binding.categoryTvLife.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvWoman.text -> binding.categoryTvWoman.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvMan.text -> binding.categoryTvMan.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvGame.text -> binding.categoryTvGame.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvBeauty.text -> binding.categoryTvBeauty.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvPet.text -> binding.categoryTvPet.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvBook.text -> binding.categoryTvBook.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            binding.categoryTvBuy.text -> binding.categoryTvBuy.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            else -> null
        }
    }

    fun setCategory(selectedCategory: String) {
        writeViewModel.setCategory(selectedCategory)
        finish()
    }
}
