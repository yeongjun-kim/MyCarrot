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

class TestChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_chat)


        val database = Firebase.database
        val ref = database.getReference("TEST")


        ref.child("YJ").addChildEventListener(object: ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var a = snapshot.getValue(UserObject::class.java)
                Log.d("fhrm", "TestChatActivity -onChildAdded(),    a: ${a}")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })

        test1.setOnClickListener {
            ref.setValue("one")
        }
        test2.setOnClickListener {
            var a = UserObject("userId", "nickname", "profileUrl", 36.5, "location",
                0,0L,0L)

            ref.child("YJ/t").setValue(a)
        }
        test3.setOnClickListener {
            ref.child("YJ").setValue(null)
        }
        test4.setOnClickListener { }
        test5.setOnClickListener { }
    }
}
