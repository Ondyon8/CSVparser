package com.example.csvparser.Data

/**
 * Mock Api service class
 * immediately returns predefined response string
 */

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

val mockResponseString = "Title1,Title2\r\nValue1,Value2"

val serviceImpl: Api by lazy {
    ApiFactory.create()
}

class ApiFactory {
    companion object {
        fun create(): Api {
            return ApiImpl()
        }
    }
}

class ApiImpl : Api {
    override suspend fun fetchCsvData(): Response<ResponseBody> {
        val mockData: ResponseBody = ResponseBody.create(MediaType.parse("text/csv"), mockResponseString)
        val mockResponse: Response<ResponseBody> = Response.success(mockData)
        return(mockResponse)
    }
}
