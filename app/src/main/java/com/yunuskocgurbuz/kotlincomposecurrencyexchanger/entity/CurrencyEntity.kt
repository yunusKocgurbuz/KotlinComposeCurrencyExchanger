package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencyData")
data class CurrencyEntity(
    @PrimaryKey val uuId: Int? = null,

    @ColumnInfo(name = "currency")
    var currency: String?,

    @ColumnInfo(name = "amount")
    var amount: Double?

) {
    /*
    @PrimaryKey(autoGenerate = true)
    var uuId : Int =

     */
}