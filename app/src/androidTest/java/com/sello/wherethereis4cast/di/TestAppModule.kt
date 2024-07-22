package com.sello.wherethereis4cast.di

import android.content.Context
import androidx.room.Room
import com.sello.wherethereis4cast.data.WeatherDao
import com.sello.wherethereis4cast.data.WeatherDatabase
import com.sello.wherethereis4cast.network.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule{
    @Provides
    @Singleton
    fun provideMockWeatherAPI(mockWebServer: MockWebServer): WeatherAPI {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer().apply { start() }
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

}