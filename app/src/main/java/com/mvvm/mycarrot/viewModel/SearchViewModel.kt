package com.mvvm.mycarrot.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {


    private val firebaseRepository: FirebaseRepository
    private val firebaseStore: FirebaseFirestore
    private var currentUserObject: MutableLiveData<UserObject>
    private var hotItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var recommendItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var categoryItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var keywordItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var keywordUserList: MutableLiveData<List<UserObject>> = MutableLiveData(listOf())
    private var categoryItemQuery: Query? = null
    private var keywordItemQuery: Query? = null
    private var keyword: String = ""

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        firebaseStore = FirebaseFirestore.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
    }

    fun getCurrentUserObject() = currentUserObject


    /*
     같은 범위 내에 모든 document 를 긁어온후, GeoPoint DESC 정렬
     result 를 lookup(조회) DESC 순으로 정렬
     result 가 10 이상일시 10개만 subList
     */
    fun getHotItemList() = hotItemList

    fun setHotItemList() {
        firebaseStore.collection("items")
            .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
            .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
            .orderBy("geoPoint", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener

                var allResultList = result.map { it.toObject(ItemObject::class.java) }
                    .sortedByDescending { it.lookup }
                if (allResultList.size > 10) allResultList = allResultList.subList(0, 10)

                hotItemList.value = allResultList
            }
    }


    /*
     같은 범위 내에 모든 document 를 긁어온후, GeoPoint ASC 정렬
     result 가 20 미만일시 그대로 set
     result 가 20 이상일시 사이즈만큼 난수를 만들고, 랜덤으로 셔플 후 20개만 추출하여 set
     */
    fun getRecommendItemList() = recommendItemList

    fun setRecommendItemList() {
        firebaseStore.collection("items")
            .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
            .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
            .orderBy("geoPoint")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener

                var allResultList = result.map { it.toObject(ItemObject::class.java) }
                if (allResultList.size > 20) {
                    var randomIndex =
                        (0 until allResultList.size).shuffled().subList(0, 20) // Make Random Index
                    allResultList = randomIndex.map { index -> allResultList[index] }
                }

                recommendItemList.value = allResultList
            }
    }

    fun getCategoryItemList() = categoryItemList
    fun setCategoryItemList(category: String) {

        if (categoryItemQuery == null) { // 처음 불렸을경우
            categoryItemQuery = firebaseStore.collection("items")
                .whereEqualTo("category", category)
                .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
                .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
                .orderBy("geoPoint")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        }

        categoryItemQuery!!.get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener // 끝까지 다 조회했을

                var tempList = mutableListOf<ItemObject>()
                result.forEach { item ->
                    tempList.add(item.toObject(ItemObject::class.java))
                }

                var beforeList = categoryItemList.value!!.toMutableList()
                beforeList.addAll(tempList)
                var afterList = beforeList.toList()

                categoryItemList.value = afterList


                // paging을 위해 다음 쿼리 미리 만들어놓는 작업
                categoryItemQuery = firebaseStore.collection("items")
                    .whereEqualTo("category", category)
                    .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
                    .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
                    .orderBy("geoPoint")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(result.documents[result.size() - 1])
                    .limit(10)
            }



        firebaseStore.collection("items")
            .whereEqualTo("category", category)
            .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
            .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
            .orderBy("geoPoint", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener

                var allResultList = result.map { it.toObject(ItemObject::class.java) }
                if (allResultList.size > 20) {
                    var randomIndex =
                        (0 until allResultList.size).shuffled().subList(0, 20) // Make Random Index
                    allResultList = randomIndex.map { index -> allResultList[index] }
                }

                recommendItemList.value = allResultList
            }
    }

    fun clearKeywordList() {
        keyword = ""
        keywordItemList.value = listOf()
        keywordUserList.value = listOf()
        keywordItemQuery = null
    }

    fun getKeyword() = keyword
    fun setKeyword(inputKeyword: String) {
        if (keyword != null && keyword != inputKeyword) {// 다른 키워드 검색하면 기존 list clear
            clearKeywordList()
        } else if (keyword == inputKeyword) return


        keyword = inputKeyword
        setKeywordItemList()
        setKeywordUserList()
    }

    fun getKeywordItemList() = keywordItemList
    fun setKeywordItemList() {
        if (keywordItemQuery == null) { // 처음 불렸을경우
            keywordItemQuery = firebaseStore.collection("items")
                .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
                .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
                .whereArrayContains("title", keyword)
                .orderBy("geoPoint")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        }

        keywordItemQuery!!.get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener
                keywordItemList.value = result.map { it.toObject(ItemObject::class.java) }

                keywordItemQuery = firebaseStore.collection("items")
                    .whereGreaterThanOrEqualTo("geoPoint", firebaseRepository.getMinGeoPoint())
                    .whereLessThanOrEqualTo("geoPoint", firebaseRepository.getMaxGeoPoint())
                    .whereArrayContains("title", keyword)
                    .orderBy("geoPoint")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(result.documents[result.size() - 1])
                    .limit(10)
            }
    }

    fun getKeywordUserList() = keywordUserList
    fun setKeywordUserList() {

        var end = keyword[keyword.length - 1]
        val newEnding = ++end

        var newString = keyword
        newString.dropLast(1)
        newString += newEnding

        firebaseStore.collection("users")
            .whereGreaterThanOrEqualTo("nickname", keyword)
            .whereLessThanOrEqualTo("nickname", newString)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener
                keywordUserList.value = result.map { it.toObject(UserObject::class.java) }
                    .filter { it.geoPoint in firebaseRepository.getMinGeoPoint()..firebaseRepository.getMaxGeoPoint() }
            }
    }


    fun test() {

    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SearchViewModel(application) as T
        }
    }
}