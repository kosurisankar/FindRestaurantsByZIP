package com.skworks.findrestaurantsbyzip

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Mock
    private val viewModel:RestaurantViewModel = mock(RestaurantViewModel::class.java)
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}