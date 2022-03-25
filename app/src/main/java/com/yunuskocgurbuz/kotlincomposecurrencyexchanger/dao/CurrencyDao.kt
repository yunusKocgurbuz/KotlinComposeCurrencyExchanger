package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity.CurrencyEntity

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencyData")
    fun getAllCurrency(): LiveData<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(item : List<CurrencyEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrency(item : CurrencyEntity)
}