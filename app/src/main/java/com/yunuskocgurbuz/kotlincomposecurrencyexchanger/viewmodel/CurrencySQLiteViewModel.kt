package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.database.CurrencyDatabase
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity.CurrencyEntity
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.repository.CurrencySQLiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencySQLiteViewModel(application: Application): AndroidViewModel(application){

    val readAllCurrency: LiveData<List<CurrencyEntity>>

    private val repository: CurrencySQLiteRepository

    init {
        val CurrencyDao = CurrencyDatabase.getInstance(application).currencyDao()
        repository = CurrencySQLiteRepository(CurrencyDao)
        readAllCurrency = repository.readAllCurrency
    }


    fun AddCurrency(item: List<CurrencyEntity>){
        viewModelScope.launch(Dispatchers.IO ) {
            repository.addCurrency(item = item)
        }
    }

    fun UpdateCurrency(item: CurrencyEntity){
        viewModelScope.launch(Dispatchers.IO ) {
            repository.updateCurrency(item = item)
        }
    }


}

class CurrencyViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(CurrencySQLiteViewModel::class.java)){
            return CurrencySQLiteViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}

