package com.mvvm.mycarrot.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.UserObject
import kotlinx.android.synthetic.main.activity_test_chat.*
import java.lang.Exception

class TestChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_chat)


        val database = Firebase.database
        var ref = database.getReference()


        test1.setOnClickListener {
            repeat(2) { i->
                repeat(2){j ->
                    var a = ref.child("/myId/yourId${i}/item${j}")
                    a.setValue("item${j}")
                }
            }
        }
        test2.setOnClickListener {
            var count =0
            ref.child("/myId/").addChildEventListener(object:ChildEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.children.forEach {
                        var a = it.value as String
                        Log.d("fhrm", "TestChatActivity -onChildAdded(),    ${it.key}: ${a}")
                    }
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            })
        }
        test3.setOnClickListener {
        }
        test4.setOnClickListener { }
        test5.setOnClickListener { }
    }
}

