package com.mywork.apps.cryptodotcom.challenge.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mywork.apps.cryptodotcom.challenge.data.AppDatabase
import com.mywork.apps.cryptodotcom.challenge.data.entity.CurrencyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val dataType = object : TypeToken<List<CurrencyInfo>>() {}.type
                        val data: List<CurrencyInfo> = Gson().fromJson(jsonReader, dataType)
                        val database = AppDatabase.getInstance(applicationContext)
                        database.currencyDao().insertAll(data)
                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "DatabaseWorker"
        const val KEY_FILENAME = "DATA_FILENAME"
    }
}
