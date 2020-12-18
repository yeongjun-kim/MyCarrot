package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.LatestGroupie
import com.mvvm.mycarrot.databinding.FragmentChatBinding
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.mvvm.mycarrot.view.ChatLogActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_rv_latestmessage.view.*

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private val mAdapter = GroupAdapter<GroupieViewHolder>()

    init {
        initLatestEventListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        binding.chatRv.adapter = mAdapter

    }

    fun initLatestEventListener(){
        val curUid = FirebaseAuth.getInstance().uid!!

        Firebase.database.reference.child("/latest-messages/${curUid}")
            .addChildEventListener(object:ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.children.forEach {

                        var item = it.getValue(LatestMessageDTO::class.java)
                        mAdapter.add(LatestGroupie(item!!, object:LatestGroupie.ClickListener{
                            override fun onClick(item:LatestMessageDTO) {
                                val intent = Intent(activity, ChatLogActivity::class.java)
                                intent.putExtra("LatestMessageDTO",item)
                                startActivity(intent)

                            }
                        }))
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // TODO("여기부터 하면됨")
                    Log.d("fhrm", "ChatFragment -onChildChanged(),    : here")
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            })
    }

}


