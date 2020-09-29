package com.mvvm.mycarrot.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_write.*
import java.util.*

class TestActivity : AppCompatActivity() {

    val PICK_PROFILE_FROM_ALBUM = 12
    var uri: Uri? = null
    var nextQ: Query? = null
    var firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val progressDialog = CustomProgressDialog(this)
    var categoryList = mutableListOf(
        "디지털/가전",
        "가구/인테리어",
        "생활/가공식품",
        "여성패션/잡화",
        "남성패션/잡화",
        "게임/취미/스포츠/레저",
        "뷰티/미용",
        "유아/반려동물용품",
        "도서/티켓/음반",
        "삽니다"
    )


    lateinit var viewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)



        test_btn1.setOnClickListener {
            test1()
        }
        test_btn2.setOnClickListener {
            test2()
        }
        test_btn3.setOnClickListener {
            test3()
        }
        test_srl.setOnRefreshListener {
            Log.d("fhrm", "TestActivity -onCreate(),    : OOJWOEFJOWEJFOJ")
            test_srl.isRefreshing = false
        }

    }

    fun test1() {
        val first = firebaseStore.collection("items")
            .limit(1)

        first.get()
            .addOnSuccessListener { result ->

                result.forEachIndexed { index, item ->
                    Log.d("fhrm", "TestActivity -test1(),    index: ${index}, item: ${item.data}")
                }
                nextQ = firebaseStore.collection("item")
                    .startAfter(result.documents[result.size() - 1])
                    .limit(10)

            }
    }


    fun test2() {
        nextQ!!.get()
            .addOnSuccessListener { result ->
                result.forEachIndexed { index, item ->
                    Log.d("fhrm", "TestActivity -test1(),    index: ${index}, item: ${item}")
                }

                nextQ = firebaseStore.collection("testCollection")
                    .orderBy("c3", Query.Direction.DESCENDING)
                    .startAfter(result.documents[result.size() - 1])
                    .limit(10)

            }
    }

    fun test3() {
        val first = firebaseStore.collection("users")
            .document("fwIMheZ1vEgT64ITZkyGkF0NE7m1")

        first.get()
            .addOnSuccessListener { result ->
                Log.d("fhrm", "TestActivity -test3(),    result.data: ${result.data}")
            }
    }

    fun testGeo() {

        var min = GeoPoint(37.40, 127.01)
        var max = GeoPoint(50.0, 127.05)

        var q = firebaseStore.collection("items")
            .whereIn("category", categoryList)
            .whereGreaterThanOrEqualTo("geoPoint", min)
            .whereLessThanOrEqualTo("geoPoint", max)
            .orderBy("geoPoint")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnSuccessListener { result ->
                result.forEach {
                    Log.d("fhrm", "TestActivity -testGeo(),    : ${it.data}")
                }
            }
    }


}
