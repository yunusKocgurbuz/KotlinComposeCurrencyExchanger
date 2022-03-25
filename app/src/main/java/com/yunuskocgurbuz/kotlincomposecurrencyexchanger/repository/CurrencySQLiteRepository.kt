package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.repository

import androidx.lifecycle.LiveData
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.dao.CurrencyDao
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity.CurrencyEntity

class CurrencySQLiteRepository(private  val currencyDao: CurrencyDao) {

    val readAllCurrency: LiveData<List<CurrencyEntity>> = currencyDao.getAllCurrency()

    suspend fun addCurrency(item: List<CurrencyEntity>){
        currencyDao.insertCurrency(item)
    }

    suspend fun updateCurrency(item: CurrencyEntity){
        currencyDao.updateCurrency(item)
    }
}