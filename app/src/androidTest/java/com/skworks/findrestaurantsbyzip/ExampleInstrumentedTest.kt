package com.skworks.findrestaurantsbyzip

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private var zipCode = 77077

    @Test
    fun testFirstScreen() {
        val mockNavController = mock(NavController::class.java)

//        rule.setContent {
//            SearchScreen(navigation = mockNavController)
//        }
        rule.setContent {
            Navigation()
//            RestaurantListScreen(zipCode)
        }
        rule.onNodeWithTag("zipCodeEt").assertExists().performClick().performTextInput(zipCode.toString())
        rule.onNodeWithText("Search").performClick()

//        verify(mockNavController).navigate("Second_Screen/${zipCode}")
    }

    @Test
    fun testSecondScreen(){
        rule.setContent {
            RestaurantListScreen(zipCode)
        }
        rule.onNodeWithTag("searchBar").assertExists()
        rule.onNodeWithTag("lazyColumn").performScrollToIndex(4)
        rule.onNodeWithTag("lazyColumn").performScrollToIndex(8)

    }
}