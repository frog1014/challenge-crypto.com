package com.mywork.apps.cryptodotcom.challenge.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mywork.apps.cryptodotcom.challenge.data.entity.CurrencyInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies")
    fun getAllCurrenciesFlow(): Flow<List<CurrencyInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CurrencyInfo>)

    @Query("DELETE FROM currencies")
    fun clear()
}
