package com.mvvm.mycarrot.room

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object DatabaseCopier {

    private val TAG = "fhrm"
    private const val DATABASE_NAME = "locationList.db"
    private var INSTANCE: AppDatabase? = null

    fun getAppDataBase(context: Context): AppDatabase?{

        if (INSTANCE == null){
            Log.d(TAG, "인스턴스 null")
            synchronized(AppDatabase::class){
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, DATABASE_NAME
                )
                    .addMigrations(AppDatabase.MIGRATION_1_2)
                    .build()
            }
        }
        else{
            Log.d(TAG, "인스턴스 null 아님")
        }
        return INSTANCE
    }


    // assets -> 앱/databases
    fun copyAttachedDatabase(context: Context) {
        Log.d(TAG, "db 카피 함수 호출")
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        Log.d(TAG, dbPath.toString())

        // db 파일 있으면
        if (dbPath.exists()) {
            Log.d(TAG, "db 카피 이미 존재")

            val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = PackageInfoCompat.getLongVersionCode(info)

            Log.d(TAG, version.toString())

            // 버전 관리 (계속 변경)
            if (version.toString() != "1"){
                Log.d(TAG, "버전 코드 다름!")
                copyDB(context, dbPath)
            }


            return
        }

        Log.d(TAG, "db 카피 존재 x")

        // 폴더 만들어주기
        dbPath.parentFile.mkdirs()
        copyDB(context, dbPath)
    }


    // Copy Function
    private fun copyDB(context: Context, _dbPath: File){
        try {
            val inputStream = context.assets.open("database/$DATABASE_NAME")
            val output = FileOutputStream(_dbPath)

            val buffer = ByteArray(8192)
            var length: Int

            while (true){
                length = inputStream.read(buffer, 0, 8192)
                if(length <= 0)
                    break
                output.write(buffer, 0, length)
            }

            output.flush()
            output.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d(TAG, "copyDB 실패!", e)
            e.printStackTrace()
        }
    }


}