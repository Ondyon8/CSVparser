package com.example.csvparser.Data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "https://raw.githubusercontent.com"

fun getNetworkService() = serviceImpl

interface Api {
    @GET("RabobankDev/AssignmentCSV/main/issues.csv")
    suspend fun fetchCsvData(): Response<ResponseBody>
}


