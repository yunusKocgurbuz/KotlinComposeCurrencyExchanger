package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.service

import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.model.ApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    //latest?access_key=72bf0c33b44c7c40a0053224facdfbd3
    @GET("latest?")
    suspend fun getCurrencyList(
        @Query("access_key") access_key: String

    ) : ApiModel


}