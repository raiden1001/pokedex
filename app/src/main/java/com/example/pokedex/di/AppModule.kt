package com.example.pokedex.di

import android.content.Context
import com.example.pokedex.data.remote.PokeApi
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(@ApplicationContext context: Context): PokeApi {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .cache(
                Cache(
                    context.cacheDir,
                    10L * 1024L * 1024L
                )
            ) // 10 MiB
            .addInterceptor(com.example.pokedex.data.remote.cache.Interceptor(context))
            .build()

        httpClient.addInterceptor(logging)
        /* if (headers.isNotEmpty()) {
             httpClient.addInterceptor(getHeadersInterceptor(headers))
         }*/

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .build()
            .create(PokeApi::class.java)
    }

    //if there are headers
    private fun getHeadersInterceptor(headers: Map<String, String>): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
            headers.forEach { header ->
                request.removeHeader(header.key)
                request.addHeader(header.key, header.value)
            }
            request.method(original.method, original.body)
            chain.proceed(request.build())
        }
    }
}