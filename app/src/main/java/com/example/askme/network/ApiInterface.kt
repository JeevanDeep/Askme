package com.example.askme.network

import android.util.Log
import com.example.askme.model.TagsResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("query={query}/query_content={query_content}")
    fun getTags(@Path("query") query: String, @Path("query_content") queryContent: String): Single<TagsResponse>

    companion object {
        private lateinit var retrofit: Retrofit
        fun getClient(): ApiInterface {

            if (!this::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.43.180:5000")
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return retrofit.create(ApiInterface::class.java)
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Log.i("logging text", message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        private fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()

        }
    }
}