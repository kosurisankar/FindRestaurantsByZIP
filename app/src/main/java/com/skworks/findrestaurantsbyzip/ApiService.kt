package com.skworks.findrestaurantsbyzip

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService
{
    @GET("restaurants/location/zipcode/{zipCode}/{pageNumber}")

    suspend fun getRestaurantsLocation(@Path("zipCode") zipCode: Int, @Path("pageNumber") pageNumber: Int) : restaurantsDataClass
//    @GET("restaurants/location/zipcode/{zipcode}/0")
//    suspend fun getRestaurantsLocation(
//        @Path("zipCode") zipCode: Int?,
//        @Header("X-RapidAPI-Key") apiKey: String,
//        @Header("X-RapidAPI-Host") apiHost: String
//    ): restaurantsDataClass
}