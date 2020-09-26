package com.mvvm.mycarrot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.viewModel.WriteViewModel
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: com.mvvm.mycarrot.databinding.ActivityCategoryBinding
    lateinit var writeViewModel: WriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        writeViewModel = ViewModelProvider(
            this, WriteViewModel.Factory(application)
        ).get(WriteViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.apply {
            av = this@CategoryActivity
            lifecycleOwner = this@CategoryActivity
        }

        initSelectedCategory()


    }

    private fun initSelectedCategory() {
        var selectedCategory = writeViewModel.getcategory().value!!

        when (selectedCategory) {
            category_digital.text -> category_digital.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_furniture.text -> category_furniture.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_life.text -> category_life.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_woman.text -> category_woman.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_man.text -> category_man.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_game.text -> category_game.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_beauty.text -> category_beauty.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_pet.text -> category_pet.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_book.text -> category_book.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            category_buy.text -> category_buy.setTextColor(
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
