package com.mvvm.mycarrot.repository

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.view.LoginActivity
import java.util.*


class FirebaseRepository private constructor() {
    /**
     *
     * loginMode: LoginActivity에서 startactvity 할 activity를 정하는 모드
     *  0: startActivity X ( 초기 )
     *  1: startActivity SignUp ( FirebaseAuth 만 가입되어있고, Firestore(users)엔 없는상황 )
     *  2: startActivity MainActivity ( FirebaseAuth , Firestore(users) 에 둘다 존재하는 상황 )
     *
     */
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore = FirebaseFirestore.getInstance()
    var curretUserObject: MutableLiveData<UserObject> = MutableLiveData()
    var loginMode: MutableLiveData<Int> = MutableLiveData(0)
    var location: MutableLiveData<String> = MutableLiveData("관악구 은천동")
    var lat = 37.55
    var long = 126.97

    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null

        @JvmStatic
        fun getInstance(): FirebaseRepository =
            instance ?: synchronized(this) {
                instance ?: FirebaseRepository().also { instance = it }
            }
    }

    init {
        initCurrentUser()
        loginMode.value = 0
    }

    private fun initCurrentUser() {
        if (firebaseAuth.currentUser == null) return

        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    curretUserObject.value = user
                    loginMode.value = 2
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginMode.value = 1
                }
            }
    }

    fun signupUserObject(nickname: String?, profileUrl: String?, location: String?) {
        var userObject = UserObject(
            firebaseAuth.currentUser!!.uid,
            nickname,
            profileUrl,
            location
        )

        firebaseStore.collection("users").document(userObject.userId!!).set(userObject)
            .addOnSuccessListener {
                curretUserObject.value = userObject
                loginMode.value = 2
            }

    }

    fun setLatLong(inputLat: Double, inputLong: Double, application: Application) {
        lat = inputLat
        long = inputLong

        // LatLong to Address
        var mGeocoder = Geocoder(application, Locale.KOREAN)
        var mResultList: List<Address>?
        mResultList = mGeocoder.getFromLocation(lat, long, 1)

        var temp = mResultList[0].getAddressLine(0).split(' ')
        var retVal = temp[2] +" "+ temp[3]

        if (!mResultList.isNullOrEmpty()) location.value = retVal
    }

    fun getCurretUser() = curretUserObject
    fun getloginMode() = loginMode
    fun getlocation() = location

}