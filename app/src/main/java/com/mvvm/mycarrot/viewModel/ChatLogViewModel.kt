package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.mvvm.mycarrot.model.MessageDTO
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository

class ChatLogViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private val db: FirebaseDatabase
    private val ref: DatabaseReference
    var message = MutableLiveData("")

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.currentUserObject
        db = Firebase.database
        ref = db.reference
    }


    fun getDb() = db
    fun getRef() = ref


    fun sendMessage(latestMessageDTO : LatestMessageDTO) {
        if (message.value == null) return

        val myId = currentUserObject.value!!.userId!!
        val yourId = latestMessageDTO.yourUid
        val yourProfileUrl = latestMessageDTO.yourProfileUrl
        val itemId = latestMessageDTO.itemUid
        val timestamp = System.currentTimeMillis()
        val message = message.value!!
        val messageType = "String"

        val myReference = ref.child("user-messages/${myId}/${yourId}/${itemId}").push()
        val yourReference = ref.child("user-messages/${myId}/${yourId}/${itemId}").push()

        val messageDTO =
            MessageDTO(myId, yourId, yourProfileUrl, timestamp, myReference.key!!, message, messageType)

        myReference.setValue(messageDTO)
        yourReference.setValue(messageDTO)

        refreshLatestMessage()
    }

    /*
    전송 버튼 누르면, latestMessage 갱신
     */
    fun refreshLatestMessage(){

    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatLogViewModel(application) as T
        }
    }
}