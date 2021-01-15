package com.mvvm.mycarrot.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Location::class), version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        @JvmField
        val MIGRATION_1_2 : Migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}

//
//@Database(entities = arrayOf(Location::class), version = 2)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun locationDao(): LocationDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
//            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
//        }
//
//        private fun buildDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java, "locationList.db"
//            ).build()
//    }
//}
