package com.mvvm.mycarrot.presentation.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mvvm.mycarrot.data.api.Api
import com.mvvm.mycarrot.data.api.ApiClient
import com.mvvm.mycarrot.data.api.NotificationBody
import com.mvvm.mycarrot.data.api.NotificationData
import com.mvvm.mycarrot.data.model.LatestMessageDTO
import com.mvvm.mycarrot.data.model.MessageDTO
import com.mvvm.mycarrot.data.model.UserObject
import com.mvvm.mycarrot.data.repository.FirebaseRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatLogViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private val db: FirebaseDatabase
    private val ref: DatabaseReference
    var message = MutableLiveData("")

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        db = Firebase.database
        ref = db.reference
    }


    fun getDb() = db
    fun getRef() = ref


    fun sendMessage(latestMessageDTO: LatestMessageDTO) {
        if (message.value.isNullOrBlank()) return

        val myId = currentUserObject.value!!.userId!!
        val yourId = latestMessageDTO.yourUid
        val opponentProfileUrl = currentUserObject.value!!.profileUrl!!
        val itemId = latestMessageDTO.itemUid
        val timestamp = System.currentTimeMillis()
        val message = message.value!!.trimStart().trim()
        val messageType = "String"

        val myReference = ref.child("/user-messages/${myId}/${yourId}/${itemId}").push()
        val yourReference = ref.child("/user-messages/${yourId}/${myId}/${itemId}").push()

        val messageDTO =
            MessageDTO(
                myId,
                yourId,
                opponentProfileUrl,
                timestamp,
                myReference.key!!,
                message,
                messageType
            )

        myReference.setValue(messageDTO)
        yourReference.setValue(messageDTO)

        sendNotificationToUser(yourId, message)
        refreshLatestMessage(latestMessageDTO)

    }

    /*
    전송 버튼 누르면, 상대방 token에 Notification 쏘기
     */
    fun sendNotificationToUser(targetUid: String, msg: String) {

        ref.child("/user-token/${targetUid}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val token = snapshot.value.toString()

                    var model = NotificationBody(
                        token,
                        NotificationData("채팅알림", "${currentUserObject.value!!.nickname}: $msg")
                    )
                    val apiService = ApiClient.client!!.create(Api::class.java)
                    val responseBodyCall = apiService.sendNotification(model)

                    responseBodyCall.enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>?,
                            response: Response<ResponseBody?>?
                        ) {
                            Log.d("fhrm", "성공, token: ${token}")
                        }

                        override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                            Log.d("fhrm", "실패")
                        }
                    })

                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    /*
    전송 버튼 누르면, latestMessage 갱신
     */
    fun refreshLatestMessage(inputDTO: LatestMessageDTO) {

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
            return ChatLogViewModel(
                application
            ) as T
        }
    }
}