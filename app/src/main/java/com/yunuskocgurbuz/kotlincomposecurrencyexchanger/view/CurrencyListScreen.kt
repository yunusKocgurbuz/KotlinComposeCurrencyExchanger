package com.yunuskocgurbuz.kotlincomposecurrencyexchanger.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.R
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.entity.CurrencyEntity
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.model.Rates
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.util.Constants.commission_fee
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.util.CurrencyName
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.viewmodel.CurrencyListViewModel
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.viewmodel.CurrencySQLiteViewModel
import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.viewmodel.CurrencyViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun CurrencyListScreen(navController: NavController) {

    //for SQLite connection
    val context = LocalContext.current
    val currencySqliteViewModel: CurrencySQLiteViewModel = viewModel(
        factory = CurrencyViewModelFactory(context.applicationContext as Application)
    )



    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val fontMy = Font(R.font.poppins_medium)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "MY BALANCES",
                fontFamily = FontFamily(fontMy),

                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)

            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(onClick = {
                    currencySqliteViewModel.AddCurrency(insertCurrencyData)
            },modifier = Modifier.padding(15.dp)
            ) {
                Text(text = "Upload money")
            }
            Spacer(modifier = Modifier.height(15.dp))
            CurrencyList(navController = navController, currencySqliteViewModel)

        }
    }

}

@Composable
fun CurrencyList(navController: NavController, currencySqliteViewModel: CurrencySQLiteViewModel, viewModel: CurrencyListViewModel = hiltViewModel()) {

    val currencyList by remember { viewModel.currencyList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                viewModel.loadCurrency()
            }
        }
    }



    LoadCurrencyList(currencyList, navController = navController, currencySqliteViewModel)

}

@Composable
fun ExchangeCurrency(currencyList: List<Rates>) {
    val fontMy = Font(R.font.poppins_medium)
    var sell by remember { mutableStateOf(0.0 )   }
    var receive by remember { mutableStateOf(0.0 )   }
    var currencyChange by remember { mutableStateOf("USD") }
    var textSell by remember { mutableStateOf("0.0") }

    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "CURRENCY EXCHANCE",
            fontFamily = FontFamily(fontMy),

            color = Color.LightGray,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)

        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "No commission fee is charged for the first 5 transactions.",
            modifier = Modifier.padding(15.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        
        Column(modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column {
                var text by remember { mutableStateOf("0.0") }
                Row(modifier = Modifier
                    .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically)  {

                    SellReceiveCircle()

                    Column {
                        TextField(modifier = Modifier.size(100.dp, 50.dp),
                            value = text,
                            onValueChange = { text = it },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                        if(!text.isEmpty()){
                            textSell = text
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = receive.toString(),
                            modifier = Modifier
                                .padding(15.dp)
                                .size(50.dp, 20.dp)

                        )
                    }




                    Column {
                        CurrencySpinner(currencyNameListSell, "EUR")

                        Spacer(modifier = Modifier.height(15.dp))

                        CurrencySpinner(currencyNameListReceive, "USD")
                        currencyChange = CurrencyName.currencyName
                        for(item in currencyList){
                            if(!text.isNullOrEmpty()){
                                sell = text.toDouble() * GetCurrency(currencyChange, item)
                            }
                        }

                        receive = sell
                    }

                }

            }



            Spacer(modifier = Modifier.height(30.dp))
            SubmitExchange(textSell.toDouble(), receive , currencyChange)



        }
        
    }

}


var commission = 5
val insertCurrencyData = listOf(
    CurrencyEntity(1, "EUR", 1000.0, commission),
    CurrencyEntity(2, "USD", 0.0, commission),
    CurrencyEntity(3, "BGN", 0.0, commission),
    CurrencyEntity(4, "JEP", 0.0, commission),
    CurrencyEntity(5, "QAR", 0.0, commission)
)

val currencyNameListSell = listOf<String>("EUR")
val currencyNameListReceive = listOf<String>("USD")

fun GetCurrency(currencyName: String, currencyList: Rates): Double{

    var currencyCount = 0.0

    when(currencyName){

        "EUR" -> currencyCount = currencyList.EUR!!.toDouble()
        "USD" -> currencyCount = currencyList.USD!!
        "BGN" -> currencyCount = currencyList.BGN!!
        "JEP" -> currencyCount = currencyList.JEP!!
        "QAR" -> currencyCount = currencyList.QAR!!
        else -> currencyCount = 0.0

    }


    return currencyCount
}


@Composable
fun CurrencySpinner (currency: List<String>, firstCurrency: String) {
    var currencyText by remember {mutableStateOf(firstCurrency)}
    var expanded by remember { mutableStateOf(false)}
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(Modifier
            .padding(5.dp)
            .clickable {
                expanded = !expanded
            }
            .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = currencyText, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
            DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                currency.forEach {
                        currency -> DropdownMenuItem(onClick = {
                    expanded = false

                    currencyText = currency

                    CurrencyName.currencyName = currencyText

                }) {
                    Text(text = currency)
                }
                }
            }
        }
    }
}



@Composable
fun LoadCurrencyList(
    currencyList: List<Rates>,
    navController: NavController,
    currencySqliteViewModel: CurrencySQLiteViewModel) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {



        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(2000)
                refreshing = false
            }
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true }
        ) {

            val getAllCurrency= currencySqliteViewModel.readAllCurrency.observeAsState(listOf()).value

            Column {



                LazyRow{
                    items(getAllCurrency){ items ->
                        CurrencyRow(navController, items)
                    }
                }


                ExchangeCurrency(currencyList)

            }

        }
    }

}



@Composable
fun CurrencyRow(navController: NavController, items: CurrencyEntity) {

    Row {
        Text(text = items.amount.toString() + " " + items.currency.toString(),
            modifier = Modifier.padding(10.dp)
            )

    }


}


@Composable
fun SubmitExchange(
    sell: Double,
    receive: Double,
    currencyChange: String
){

    //for SQLite connection
    val context = LocalContext.current
    val currencySqliteViewModel: CurrencySQLiteViewModel = viewModel(
        factory = CurrencyViewModelFactory(context.applicationContext as Application)
    )

    val getAllCurrency= currencySqliteViewModel.readAllCurrency.observeAsState(listOf()).value
    var newSell = 0.0
    var newReceive = 0.0
    var note = ""
    var commission_new by remember { mutableStateOf(0) }



    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false)  }

            Button(onClick = {
                if(sell > 0){
                    for(i in getAllCurrency){
                        if(i.currency.equals("EUR")){
                            commission_new = i.commission!!
                            println(commission_new)
                            var getAmount = i.amount!!
                            if(i.amount != 0.0 && getAmount >= sell){
                                if(commission_new > 0){
                                    newSell = getAmount - sell
                                }else{
                                    newSell = getAmount - (sell + commission_fee)
                                }

                            }else {
                                note = "Insufficient Balance!!"
                                newSell = getAmount
                            }
                        }else if(i.currency.equals("USD")){
                            var getAmount = i.amount!!
                            if(note.isEmpty()){

                                newReceive = getAmount + receive
                            }else{
                                newReceive = getAmount
                            }

                        }
                    }
                    openDialog.value = true

                }else{
                    Toast.makeText(context,"Please enter a number greater than zero.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
                modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(Color.Cyan)) {
                Text("SUBMIT")
            }

            if (openDialog.value) {
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Currency Converted")
                    },
                    text = {
                        if(note.isEmpty()){
                            if(commission_new > 0){
                                Text("You have converted $sell" + " EUR to $receive" +  " $currencyChange")
                            }else{
                                Text("You have converted $sell" + " EUR to $receive" +  " $currencyChange. Commission fee $commission_fee EUR")
                            }

                        }else{
                            Text(note)
                        }

                    },
                    confirmButton = {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp)),
                            colors = ButtonDefaults.buttonColors(Color.Cyan),
                            onClick = {
                                if(note.isEmpty()){

                                    commission_new -= 1

                                   // println(commission_new)

                                    val updateCurrencySell = CurrencyEntity(1, "EUR", newSell, commission_new)
                                    val updateCurrencyReceive = CurrencyEntity(2, currencyChange, newReceive, commission_new)
                                    currencySqliteViewModel.UpdateCurrency(updateCurrencySell)
                                    currencySqliteViewModel.UpdateCurrency(updateCurrencyReceive)

                                    openDialog.value = false
                                    Toast.makeText(context,"Currency convert completed...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                            Text("OK")
                        }
                    }

                )
            }
        }

    }
}



@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column() {
        Text(error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { onRetry }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }
    }
}


@Composable
fun SellReceiveCircle() {
    Column  {

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = CircleShape,
            modifier = Modifier
                .size(50.dp, 50.dp)
                .clip(CircleShape)
                .background(Color.Red),
        ) {
            Icon(
                Icons.Outlined.KeyboardArrowUp,
                contentDescription = "Sell",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            shape = CircleShape,
            modifier = Modifier
                .size(50.dp, 50.dp)
                .clip(CircleShape)
                .background(Color.Green),
        ) {
            Icon(
                Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Receive",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

    }

    Column {
        Text(text = "Sell",
            modifier = Modifier.padding(15.dp))

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Receive",
            modifier = Modifier.padding(15.dp))

    }
}

