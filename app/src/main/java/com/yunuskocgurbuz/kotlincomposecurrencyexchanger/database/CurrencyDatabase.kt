package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.dao.CurrencyDao
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity.CurrencyEntity


@Database(entities = [CurrencyEntity::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase: RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object{
        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyDatabase::class.java,
                    "currency_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}