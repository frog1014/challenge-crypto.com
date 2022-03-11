package com.mywork.apps.cryptodotcom.challenge.data

import com.mywork.apps.cryptodotcom.challenge.data.dao.CurrencyDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
) {

    fun getAllCurrenciesFlow() = currencyDao.getAllCurrenciesFlow()

    companion object {
        const val TAG = "CurrencyRepository"
    }
}