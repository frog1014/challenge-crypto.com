package com.mywork.apps.cryptodotcom.challenge.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyInfo(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val symbol: String,
    val name: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val modifiedAt: Long,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Long,
) {

    override fun toString() = name

    companion object {
        fun newInstance(
            id: String,
            symbol: String,
            name: String,
            now: Long = System.currentTimeMillis()
        ): CurrencyInfo =
            CurrencyInfo(id = id, symbol = symbol, name = name, now, now)
    }

}