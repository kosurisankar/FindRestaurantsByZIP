package com.skworks.findrestaurantsbyzip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skworks.findrestaurantsbyzip.RetrofitInstance.retrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RestaurantViewModel() : ViewModel() {
    private val _restaurants = MutableStateFlow<List<RestaurantsList>>(emptyList())
    val restaurants: StateFlow<List<RestaurantsList>> = _restaurants

    var currentPage = 0
    private var isLoading = false
    var hasMorePages = true

    fun fetchRestaurants(zipCode: Int, page: Int) {
        if (isLoading || !hasMorePages) return // Prevent duplicate loading
        isLoading = true
        viewModelScope.launch {
            try {
//                val response = retrofit.getRestaurantsLocation(zipCode, page)
                val response = RestaurantsDataClass(
                    restaurants = listOf(
                        RestaurantsList(
                            restaurantName = "Wally's",
                            address = "447 North Canon Drive",
                            cityName = "Beverly Hills",
                            hoursInterval = "Sun - Wed (10:00 AM - 12:00 AM) | Thu - Sat (10:00 AM - 1:00 AM)",
                            cuisineType = "American,Wine Bar"
                        ),
                        RestaurantsList(
                            restaurantName = "Laduree",
                            address = "311 N Beverly Dr",
                            cityName = "Beverly Hills",
                            hoursInterval = "Sun - Sat (9:00 AM - 7:00 PM)",
                            cuisineType = "Cafe,French"
                        )
                    )
                )
                // Appending new data to the existing list
                _restaurants.value += response.restaurants

                // Update flags
                currentPage = page
                isLoading = false
                hasMorePages = response.restaurants.isNotEmpty()
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error fetching items: ${e.message}")
                isLoading = false
            }
        }
    }
}