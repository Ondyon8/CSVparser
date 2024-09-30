package com.example.csvparser.Data

import okhttp3.OkHttpClient
import retrofit2.Retrofit

val serviceImpl: Api by lazy {
    ApiFactory.create()
}

class ApiFactory {
    companion object {
        fun create(): Api {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()

            return retrofit.create(Api::class.java)
        }
    }
}