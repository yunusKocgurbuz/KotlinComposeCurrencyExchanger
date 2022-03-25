package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.repository

import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.model.ApiModel
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.service.CurrencyAPI
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.util.Constants.API_KEY
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CurrencyApiRepository @Inject constructor(
    private val api: CurrencyAPI
){
    suspend fun getCurrencyList(): Resource<ApiModel> {
        val response = try {
            api.getCurrencyList(API_KEY)

        }catch (e: Exception){
            return Resource.Error("Error API connect!!!" + e.toString())

        }


        return Resource.Success(response)
    }

}