package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.model.Rates
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.repository.CurrencyApiRepository
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor (
    private val repository: CurrencyApiRepository
): ViewModel() {

    var currencyList = mutableStateOf<List<Rates>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadCurrency()
    }

    fun loadCurrency(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCurrencyList()
            when(result){
                is Resource.Success -> {
                    val newItems = result.data!!.rates!!
                    errorMessage.value = ""
                    isLoading.value = false
                    currencyList.value += newItems

                }

                is Resource.Error -> {

                    errorMessage.value = result.message!!
                    isLoading.value = false

                }
            }

        }
    }

}