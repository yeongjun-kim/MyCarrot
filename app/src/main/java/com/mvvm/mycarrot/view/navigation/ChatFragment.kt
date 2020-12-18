package com.mvvm.mycarrot.view.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.viewModel.HomeViewModel

class ChatFragment : Fragment() {

    lateinit var a: ChildEventListener
    var db = Firebase.database
    var rf = db.getReference("TEST")

    init {
        var count = 0

        a = rf.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var a = snapshot.value as String
                Log.d("fhrm", "ChatFragment -onChildAdded(),    a: ${count++}")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fhrm", "ChatFragment -onCreate(),    : here")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rf.removeEventListener(a)
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    fun initDatabaseListener(){
        val db = Firebase.database
        val ref = db.getReference("/latestMessage/")
    }

}
