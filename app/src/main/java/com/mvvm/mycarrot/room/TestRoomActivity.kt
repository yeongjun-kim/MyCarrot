package com.mvvm.mycarrot.room

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.activity_test_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class TestRoomActivity : AppCompatActivity() {

    lateinit var vm: LocationViewModel
    var a = mutableListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_room)


        vm = ViewModelProvider(
            this,
            LocationViewModel.Factory(application)
        ).get(LocationViewModel::class.java)


        vm.getAll().observe(this, Observer {
//            Log.d("fhrm", "TestRoomActivity -onCreate(),    : ${it}")
        })

        vm.getLikeQuery().observe(this, Observer {
            it.forEach { Log.d("fhrm", "TestRoomActivity -onCreate(),    : ${it.str}") }
        })


        var job = CoroutineScope(Dispatchers.IO).launch{
            DatabaseCopier.copyAttachedDatabase(context = applicationContext)
        }

        runBlocking {
            job.join()
        }


        roomBtn1.setOnClickListener {
            Log.d("fhrm", "TestRoomActivity -onCreate(),    vm.getAll().value.size: ${vm.getAll().value!!.size}")
        }

        roomBtn2.setOnClickListener {

            var q = roomEt.text.toString()
            vm.setLikeQuery(q)

        }

    }







}
