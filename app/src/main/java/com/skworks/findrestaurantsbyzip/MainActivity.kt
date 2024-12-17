package com.skworks.findrestaurantsbyzip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.skworks.findrestaurantsbyzip.ui.theme.FindRestaurantsByZIPTheme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindRestaurantsByZIPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun SearchScreen(navigation: NavController) {
    var zip by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.firstscreen),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3F
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.reshot_icon_square_food),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(70.dp))
            TextField(
                value = zip,
                onValueChange = { zip = it },
                placeholder = { Text(text = "Enter Your Zip Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
               modifier = Modifier.testTag("zipCodeEt")
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                onClick = { if (zip.isNotEmpty()) navigation.navigate("Second_Screen/${zip}") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(
                        id = R.color.bt_search
                    ), contentColor = Color.Black
                ),
                enabled = zip.isNotEmpty()
            ) {
                Text(text = "Search")
            }

        }
    }
}

@Composable
fun RestaurantListScreen(zipCode: Int, viewModel: RestaurantViewModel = viewModel()) {
    val restaurants by viewModel.restaurants.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.fetchRestaurants(zipCode, 0)
    }
    val filteredRestaurants = if (searchQuery.isEmpty()) {
        restaurants
    } else {
        restaurants.filter { it.restaurantName.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.Center
    ) {
        Scaffold(topBar = {
            SearchBarWithIcon(searchQuery) { query ->
                searchQuery = query
            }
        }, modifier = Modifier.testTag("searchBar")) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.firstscreen),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.5F
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .testTag("lazyColumn")
                ) {
                    items(filteredRestaurants) { res ->
                        RestaurantItem(res)
                    }
                    item {
                        if (viewModel.hasMorePages) {
                            LaunchedEffect(Unit) {
                                viewModel.fetchRestaurants(zipCode, viewModel.currentPage + 1)
                            }
                        } else {
                            Text(
                                text = "No more restaurants to load.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: RestaurantsList) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp, end = 16.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                ), shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "${restaurant.restaurantName} ", fontWeight = FontWeight.Bold)
            Text(text = "City: ${restaurant.cityName}")
            Text(text = "Address: ${restaurant.address}")
            Text(text = "Type: ${restaurant.cuisineType}")
            Text(text = "Opens At: ${restaurant.hoursInterval}")
        }

    }
}

@Composable
fun SearchBarWithIcon(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search Restaurants") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun Navigation() {
    val navigation = rememberNavController()
    NavHost(navController = navigation, startDestination = "First_Screen") {
        composable("First_Screen") { SearchScreen(navigation) }
        composable(
            "Second_Screen/{zipCode}",
            arguments = listOf(navArgument("zipCode") { type = NavType.IntType })
        ) { backStack ->
            val zipCode = backStack.arguments!!.getInt("zipCode")
            RestaurantListScreen(zipCode)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindRestaurantsByZIPTheme {
//        Navigation()
        SearchScreen(rememberNavController())
//        RestaurantListScreen(4, viewModel = RestaurantViewModel())
    }
}

object RetrofitInstance {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "x-rapidapi-key", "ecc3f4b96emsh7052e4e5c130349p18db95jsn570e6cdc4721"
            )  // Replace with your actual token
                .addHeader(
                    "x-rapidapi-host", "restaurants-near-me-usa.p.rapidapi.com"
                )  // Replace with your actual token
                .addHeader(
                    "Content-Type", "application/json"
                ).build()
            chain.proceed(request)
        }.build()
    }
    val BASE_URL = "https://restaurants-near-me-usa.p.rapidapi.com/"

    val retrofit: ApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

}
