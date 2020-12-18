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
        if (message.value.isNullOrBlank()) return

        val myId = currentUserObject.value!!.userId!!
        val yourId = latestMessageDTO.yourUid
        val opponentProfileUrl = currentUserObject.value!!.profileUrl!!
        val itemId = latestMessageDTO.itemUid
        val timestamp = System.currentTimeMillis()
        val message = message.value!!
        val messageType = "String"

        val myReference = ref.child("/user-messages/${myId}/${yourId}/${itemId}").push()
        val yourReference = ref.child("/user-messages/${yourId}/${myId}/${itemId}").push()

        val messageDTO =
            MessageDTO(myId, yourId, opponentProfileUrl, timestamp, myReference.key!!, message, messageType)

        myReference.setValue(messageDTO)
        yourReference.setValue(messageDTO)

        refreshLatestMessage(latestMessageDTO)
    }

    /*
    전송 버튼 누르면, latestMessage 갱신
     */
    fun refreshLatestMessage(inputDTO:LatestMessageDTO){

        val myId = currentUserObject.value!!.userId!!
        val yourId = inputDTO.yourUid
        val itemId = inputDTO.itemUid

        val saveToMeLatestDTO = LatestMessageDTO(
            inputDTO.myUid,
            inputDTO.yourUid,
            inputDTO.itemUid,
            inputDTO.opponentNickname,
            inputDTO.opponentProfileUrl,
            inputDTO.opponentLocation,
            inputDTO.opponentTemperature,
            inputDTO.itemName,
            inputDTO.itemPrice,
            inputDTO.itemImageUrl,
            System.currentTimeMillis(),
            message.value!!,
            "String"
        )

        val saveToYouLatestDTO = LatestMessageDTO(
            inputDTO.yourUid,
            currentUserObject.value!!.userId!!,
            inputDTO.itemUid,
            currentUserObject.value!!.nickname!!,
            currentUserObject.value!!.profileUrl!!,
            currentUserObject.value!!.location!!,
            currentUserObject.value!!.temperature!!,
            inputDTO.itemName,
            inputDTO.itemPrice,
            inputDTO.itemImageUrl,
            System.currentTimeMillis(),
            message.value!!,
            "String"
        )

        val myReference = ref.child("/latest-messages/${myId}/${yourId}/${itemId}")
        val yourReference = ref.child("/latest-messages/${yourId}/${myId}/${itemId}")

        myReference.setValue(saveToMeLatestDTO)
        yourReference.setValue(saveToYouLatestDTO)

        message.value = null
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatLogViewModel(application) as T
        }
    }
}