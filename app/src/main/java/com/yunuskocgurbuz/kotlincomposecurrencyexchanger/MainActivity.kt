package com.yunuskocgurbuz.kotlincomposecurrencyexchanger

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.ui.theme.KotlinComposeCurrencyExchangerTheme
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.view.CurrencyListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinComposeCurrencyExchangerTheme {

                val navController = rememberNavController()
                NavHost(navController =  navController, startDestination = "currency_list_screen"){
                    composable("currency_list_screen"){

                        CurrencyListScreen(navController)
                        val activity = (LocalContext.current as? Activity)
                        BackHandler(true) {
                            activity?.finish()
                        }

                    }
                }
            }
        }
    }
}
