package com.mvvm.mycarrot.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.firebase.firestore.GeoPoint
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.test.firestoreItem
import kotlinx.android.synthetic.main.activity_test_room.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.InputStream
import java.math.BigDecimal

class TestRoomActivity : AppCompatActivity() {

    lateinit var vm:LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_room)


        vm = ViewModelProvider(
            this,
            LocationViewModel.Factory(application)
        ).get(LocationViewModel::class.java)


        vm.getAll().observe(this, Observer {
        })


        roomBtn.setOnClickListener {
            excel()
        }
    }


    private fun excel() {
        try {
            val myInput: InputStream
            val assetManager = assets             // assetManager 초기 설정
            myInput = assetManager.open("locationList.xls")            //  엑셀 시트 열기
            val myFileSystem = POIFSFileSystem(myInput)            // POI File System 객체 만들기
            val myWorkBook = HSSFWorkbook(myFileSystem)            //워크 북
            val sheet = myWorkBook.getSheetAt(0)            // 워크북에서 시트 가져오기
            val rowIter = sheet.rowIterator()            //행을 반복할 변수 만들어주기
            var rowno = 0            //행 넘버 변수 만들기


            var a = mutableSetOf<String>()

            rowIter.hasNext() // 1행은 컬럼명이기때문에 한번 넘김
            while (rowIter.hasNext()) {            //행 반복문

                val myRow = rowIter.next() as HSSFRow
                if (rowno != 0) {
                    val cellIter = myRow.cellIterator()
                    var colno = 0

                    var l1: String = ""
                    var l2: String = ""
                    var l3: String = ""
                    var l4: String = ""
                    var lat: Double = 0.0
                    var long: Double = 0.0

                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next() as HSSFCell
                        if (colno === 0) l1 = myCell.toString()
                        else if (colno === 1) l2 = myCell.toString()
                        else if (colno === 2) l3 = myCell.toString()
                        else if (colno === 3) l4 = myCell.toString()
                        else if (colno === 4) lat = myCell.toString().toDouble()
                        else if (colno === 5) long = myCell.toString().toDouble()

                        colno++
                    }

                    var location = listOf(l1,l2,l3,l4).filter { it.isNotBlank() }.joinToString(" ")

                    vm.insert(Location(location,lat,long))

                }
                rowno++
            }

        } catch (e: Exception) {
            Log.d("fhrm", "TestActivity -excelToItemList(),    e: ${e.message}")
        }

    }
}
