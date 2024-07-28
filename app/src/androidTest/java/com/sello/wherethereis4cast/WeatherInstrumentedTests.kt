package com.sello.wherethereis4cast

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sello.wherethereis4cast.data.WeatherDataPOJO
import com.sello.wherethereis4cast.repository.WeatherApiRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class WeatherInstrumentedTests {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var weatherApiRepository: WeatherApiRepository

    @Inject
    lateinit var mockWebServer: MockWebServer

    private val responseBody = """
            {
                "city": {
                    "id": 993800,
                    "coord": {"lat": -26.2023, "lon": 28.0436},
                    "country": "ZA",
                    "name": "Johannesburg",
                    "population": 2026469,
                    "timezone": 7200
                },
                "cod": "200",
                "message": 6.6668546,
                "cnt": 7,
                "list": [
                    {
                        "dt": 1721642400,
                        "sunrise": 1721623910,
                        "sunset": 1721662583,
                        "temp": {
                            "day": 15.51,
                            "min": 9.92,
                            "max": 19.24,
                            "night": 13.52,
                            "eve": 17.18,
                            "morn": 10.73
                        },
                        "feels_like": {
                            "day": 14.29,
                            "night": 11.84,
                            "eve": 15.74,
                            "morn": 8.49
                        },
                        "pressure": 1022,
                        "humidity": 45,
                        "speed": 5.05,
                        "deg": 304,
                        "gust": 9.69,
                        "clouds": 2,
                        "pop": 5.05,
                        "rain": 5.05,
                        "weather": [
                            {
                                "id": 800,
                                "main": "Clear",
                                "description": "sky is clear",
                                "icon": "01d"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

    @Before
    fun setup() {
        hiltRule.inject()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun should_get_weather_forecast_using_city_name(): Unit = runBlocking {

        val result = weatherApiRepository
            .getWeatherUpdate("Johannesburg", WeatherDataPOJO(loading = true))
        assertEquals("Johannesburg", result.data?.city?.name)
        assertEquals(15.51, result.data?.list?.get(0)?.temp?.day)
        assertTrue(result.isSearchedFromTextFieldLocationFound!!)
    }

    @Test
    fun should_get_weather_forecast_using_coordinates(): Unit = runBlocking {

        val result = weatherApiRepository.getWeatherUpdate(
            latitude = -26.2023,
            longitude = 28.0436,
            WeatherDataPOJO(loading = true)
        )
        assertEquals("Johannesburg", result.data?.city?.name)
        assertEquals(15.51, result.data?.list?.get(0)?.temp?.day)
        assertEquals(result.isSearchedFromTextFieldLocationFound, null)
    }

}
