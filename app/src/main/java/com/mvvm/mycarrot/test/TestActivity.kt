package com.mvvm.mycarrot.test

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.activity_test.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.InputStream
import java.math.BigDecimal


class TestActivity : AppCompatActivity() {

    var uri: Uri? = null
    var itemList: MutableList<firestoreItem> = mutableListOf()            //MutableList 생성
    var userList: MutableList<firestoreUser> = mutableListOf()            //MutableList 생성


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        //********************************** DEFAULT **********************************//


        dummy_items.setOnClickListener {
            excelToItemList()
        }
        dummy_users.setOnClickListener {
            excelToUserList()
        }
        aa1.setOnClickListener {

        }
        aa2.setOnClickListener {
        }

        // *************************************************************************** //


    }


    private fun excelToItemList() {
        try {
            val myInput: InputStream
            val assetManager = assets             // assetManager 초기 설정
            myInput = assetManager.open("items.xls")            //  엑셀 시트 열기
            val myFileSystem = POIFSFileSystem(myInput)            // POI File System 객체 만들기
            val myWorkBook = HSSFWorkbook(myFileSystem)            //워크 북
            val sheet = myWorkBook.getSheetAt(0)            // 워크북에서 시트 가져오기
            val rowIter = sheet.rowIterator()            //행을 반복할 변수 만들어주기
            var rowno = 0            //행 넘버 변수 만들기


            rowIter.hasNext() // 1행은 컬럼명이기때문에 한번 넘김
            while (rowIter.hasNext()) {            //행 반복문

                val myRow = rowIter.next() as HSSFRow
                if (rowno != 0) {
                    //열을 반복할 변수 만들어주기
                    val cellIter = myRow.cellIterator()
                    //열 넘버 변수 만들기
                    var colno = 0
                    var c1_itemid = ""
                    var c2_likecount = ""
                    var c3_lookup = ""
                    var c4_overview1 = ""
                    var c5_overview2 = ""
                    var c6_price = ""
                    var c7_timestamp = ""
                    var c8_title = ""
                    var c9_userid = ""
                    var c10_userlocation = ""
                    var c11_lat = ""
                    var c12_long = ""
                    var c13_category = ""
                    var c14_url = arrayListOf<String>()
                    //열 반복문
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as HSSFCell
                        if (colno === 0) {
                            c1_itemid = myCell.toString()
                        } else if (colno === 1) {
                            c2_likecount = myCell.toString()
                            if (c2_likecount == "0.0") c2_likecount = "0"
                        } else if (colno === 2) {
                            c3_lookup = myCell.toString()
                            if (c3_lookup == "0.0") c3_lookup = "0"
                        } else if (colno === 3) {
                            c4_overview1 = myCell.toString()
                        } else if (colno === 4) {
                            c5_overview2 = myCell.toString()
                        } else if (colno === 5) {
                            c6_price = myCell.toString()
                        } else if (colno === 6) {
                            c7_timestamp = myCell.toString()
                        } else if (colno === 7) {
                            c8_title = myCell.toString()
                        } else if (colno === 8) {
                            c9_userid = myCell.toString()
                        } else if (colno === 9) {
                            c10_userlocation = myCell.toString()
                        } else if (colno === 10) {
                            c11_lat = myCell.toString()
                        } else if (colno === 11) {
                            c12_long = myCell.toString()
                        } else if (colno === 12) {
                            c13_category = myCell.toString()
                        } else if (colno === 13) {
                            c14_url.add(myCell.toString())
                        } else {
                            c14_url.add(myCell.toString())
                        }

                        colno++
                    }
                    itemList.add(
                        firestoreItem(
                            c1_itemid,
                            c9_userid,
                            "당근이${c9_userid.split("_")[1]}",
                            c10_userlocation,
                            listOf("sell", "sell", "sell", "soldout", "reservation").random(),
                            c14_url,
                            c8_title.split(" "),
                            c13_category,
                            "$c4_overview1  $c5_overview2",
                            c3_lookup.split(".")[0].toLong(),
                            c6_price.split(".")[0],
                            GeoPoint(c11_lat.toDouble(), c12_long.toDouble()),
                            BigDecimal(c7_timestamp.toDouble()).toLong(),
                            c2_likecount.toLong(),
                            arrayListOf()
                        )
                    )
                }
                rowno++
            }
            itemCommitToFirestore()
        } catch (e: Exception) {
            Log.d("fhrm", "TestActivity -excelToItemList(),    e: ${e.message}")
        }

    }


    private fun excelToUserList() {
        try {
            val myInput: InputStream
            val assetManager = assets             // assetManager 초기 설정
            myInput = assetManager.open("users.xls")            //  엑셀 시트 열기
            val myFileSystem = POIFSFileSystem(myInput)            // POI File System 객체 만들기
            val myWorkBook = HSSFWorkbook(myFileSystem)            //워크 북
            val sheet = myWorkBook.getSheetAt(0)            // 워크북에서 시트 가져오기
            val rowIter = sheet.rowIterator()            //행을 반복할 변수 만들어주기
            var rowno = 0            //행 넘버 변수 만들기


            rowIter.hasNext() // 1행은 컬럼명이기때문에 한번 넘김
            while (rowIter.hasNext()) {            //행 반복문

                val myRow = rowIter.next() as HSSFRow
                if (rowno != 0) {
                    //열을 반복할 변수 만들어주기
                    val cellIter = myRow.cellIterator()
                    //열 넘버 변수 만들기
                    var colno = 0
                    var c1_lat = ""
                    var c2_long = ""
                    var c3_location = ""
                    var c4_nickname = ""
                    var c5_userid = ""
                    var c6_profileUrl = ""
                    var c7_itemList: ArrayList<String> = arrayListOf()
                    var c8_likeList: ArrayList<String> = arrayListOf()
                    var isItemList = true
                    //열 반복문
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as HSSFCell
                        if (colno === 0) {
                            c1_lat = myCell.toString()
                        } else if (colno === 1) {
                            c2_long = myCell.toString()
                        } else if (colno === 2) {
                            c3_location = myCell.toString()
                        } else if (colno === 3) {
                            c4_nickname = myCell.toString()
                        } else if (colno === 4) {
                            c5_userid = myCell.toString()
                        } else if (colno === 5) {
                            c6_profileUrl = myCell.toString()
                        } else {
                            if (myCell.toString() == "endItemList") isItemList = false
                            else if (isItemList) c7_itemList.add(myCell.toString())
                            else if (!isItemList) c8_likeList.add(myCell.toString())
                        }
                        colno++
                    }
                    userList.add(
                        firestoreUser(
                            c5_userid,
                            c4_nickname,
                            c6_profileUrl,
                            36.5,
                            c3_location,
                            (1..100).random(),
                            (1600000000000..System.currentTimeMillis()).random(),
                            (1600000000000..System.currentTimeMillis()).random(),
                            GeoPoint(c1_lat.toDouble(), c2_long.toDouble()),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            (0..200).random(),
                            arrayListOf(),
                            c7_itemList,
                            c8_likeList
                        )
                    )
                }
                rowno++
            }
            userCommitToFirestore()
        } catch (e: Exception) {
            Log.d("fhrm", "TestActivity -excelToItemList(),    e: ${e.message}")
        }

    }


    private fun itemCommitToFirestore() {
        val qry = FirebaseFirestore.getInstance().collection("items")


        itemList.forEachIndexed { index, item ->
            qry.document(item.id)
                .set(item)
                .addOnSuccessListener { documentReference ->
                    Log.d("fhrm", "저장 성공 ${index}")
                }
                .addOnFailureListener { e ->
                    Log.e("fhrm", "Error adding document", e)
                }
        }
    }

    private fun userCommitToFirestore() {
        val qry = FirebaseFirestore.getInstance().collection("users")

        userList.forEach {
            var positive = it.positive_1 + it.positive_2 + it.positive_3 + it.positive_4
            var negative = it.negative_1 + it.negative_2 + it.negative_3 + it.negative_4

            var newTemperature = 36.5 + String.format(
                "%.1f",
                (positive.toDouble() - negative.toDouble()) / 50
            ).toDouble()
            it.temperature = newTemperature
        }

        userList.forEachIndexed { index, item ->

            qry.document(item.userId!!)
                .set(item)
                .addOnSuccessListener { documentReference ->
                    Log.d("fhrm", "저장 성공 ${index}")
                }
                .addOnFailureListener { e ->
                    Log.e("fhrm", "Error adding document", e)
                }
        }
    }


}

data class firestoreItem(
    var id: String = "",
    var userId: String = "",
    var userNickname: String = "",
    var userLocation: String = "",
    var status: String = "",
    var imageList: ArrayList<String> = arrayListOf(),
    var title: List<String> = listOf(),
    var category: String = "",
    var overview: String = "",
    var lookup: Long = 0,
    var price: String = "",
    var geoPoint: GeoPoint = GeoPoint(0.0, 0.0),
    var timestamp: Long = 0,
    var likeCount: Long = 0,
    var chatList: ArrayList<String> = arrayListOf()
)

data class firestoreUser(
    var userId: String? = null,
    var nickname: String? = null,
    var profileUrl: String? = null,
    var temperature: Double? = null,
    var location: String? = null,
    var locationCertification: Int? = null,
    var joined: Long? = null,
    var lastLoginTime: Long? = null,
    var geoPoint: GeoPoint = GeoPoint(37.55, 126.97), // Default 관악구
    var positive_1: Int = 0,
    var positive_2: Int = 0,
    var positive_3: Int = 0,
    var positive_4: Int = 0,
    var negative_1: Int = 0,
    var negative_2: Int = 0,
    var negative_3: Int = 0,
    var negative_4: Int = 0,
    var likeUserList: ArrayList<String> = arrayListOf(),
    var itemList: ArrayList<String> = arrayListOf(),
    var likeList: ArrayList<String> = arrayListOf()
)