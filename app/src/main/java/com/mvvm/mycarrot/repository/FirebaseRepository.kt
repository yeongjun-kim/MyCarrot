package com.mvvm.mycarrot.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.view.LoginActivity


class FirebaseRepository private constructor(){
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

    companion object{
        @Volatile private var instance:FirebaseRepository? = null
        @JvmStatic fun getInstance():FirebaseRepository =
                instance ?: synchronized(this){
                    instance?: FirebaseRepository().also { instance = it }
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
                    Log.d("fhrm", "FirebaseRepository -initCurrentUser(),    이미 있음")
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginMode.value = 1
                    Log.d("fhrm", "FirebaseRepository -firebaseAuthWithGoogle(),    loginMode.value = 1")
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
                Log.d("fhrm", "FirebaseRepository -signupUserObject(),    새로 만들어서 들어옴, ${loginMode.value}")
                Log.d("fhrm", "FirebaseRepository -signupUserObject(),    ${loginMode.value}")
                curretUserObject.value = userObject
            }
        loginMode.value = 2
        Log.d("fhrm", "FirebaseRepository -firebaseAuthWithGoogle(),    loginMode.value = 2")

    }

    fun getCurretUser() = curretUserObject
    fun getloginMode() = loginMode


}