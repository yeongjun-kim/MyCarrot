package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private val mAdapter = GroupAdapter<GroupieViewHolder>()
    val latestMessagesMap = HashMap<String, LatestMessageDTO>()

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

    private fun refreshRecyclerViewMessages() {
        mAdapter.clear()
        latestMessagesMap.values.sortedByDescending { it.timestamp }.forEach {
            mAdapter.add(LatestGroupie(it, object : LatestGroupie.ClickListener {
                override fun onClick(item: LatestMessageDTO) {
                    val intent = Intent(activity, ChatLogActivity::class.java)
                    intent.putExtra("LatestMessageDTO", item)
                    startActivity(intent)

                }
            }))
        }
    }

    fun initLatestEventListener() {
        val curUid = FirebaseAuth.getInstance().uid!!

        Firebase.database.reference.child("/latest-messages/${curUid}")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    snapshot.children.forEach {
                        val message = it.getValue(LatestMessageDTO::class.java) ?: return
                        latestMessagesMap[it.key!!] = message
                        refreshRecyclerViewMessages()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.children.forEach {
                        val message = it.getValue(LatestMessageDTO::class.java) ?: return
                        latestMessagesMap[it.key!!] = message
                        refreshRecyclerViewMessages()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
            })
    }

}


