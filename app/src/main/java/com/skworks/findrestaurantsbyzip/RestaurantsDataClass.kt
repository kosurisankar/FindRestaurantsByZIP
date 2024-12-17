package com.skworks.findrestaurantsbyzip

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantsDataClass(
    val restaurants: List<RestaurantsList>
)
data class RestaurantsList (
    val restaurantName : String,
    val cityName : String,
    val cuisineType : String,
    val hoursInterval : String,
    val  address: String,
)