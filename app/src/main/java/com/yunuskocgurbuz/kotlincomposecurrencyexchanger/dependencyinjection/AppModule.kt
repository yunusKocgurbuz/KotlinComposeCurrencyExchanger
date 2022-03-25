package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.dependencyinjection


import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.repository.CurrencyApiRepository
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.service.CurrencyAPI
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideCurrencyRepository(
        api: CurrencyAPI
    ) = CurrencyApiRepository(api)

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyAPI {

        var okHttpClient: OkHttpClient? = null
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
            .create(CurrencyAPI::class.java)
    }


}