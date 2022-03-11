package com.mywork.apps.cryptodotcom.challenge.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mywork.apps.cryptodotcom.challenge.data.dao.CurrencyDao
import com.mywork.apps.cryptodotcom.challenge.data.entity.CurrencyInfo
import com.mywork.apps.cryptodotcom.challenge.utilities.DATABASE_NAME
import com.mywork.apps.cryptodotcom.challenge.utilities.DATA_FILENAME
import com.mywork.apps.cryptodotcom.challenge.workers.DatabaseWorker
import com.mywork.apps.cryptodotcom.challenge.workers.DatabaseWorker.Companion.KEY_FILENAME

@Database(
    entities = [CurrencyInfo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<DatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )

                .build()

        private fun buildTestDatabase(context: Context): AppDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<DatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )

                .build()
    }
}
