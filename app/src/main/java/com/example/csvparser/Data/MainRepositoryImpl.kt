package com.example.csvparser.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.csvparser.Data.Models.CsvLine
import com.example.csvparser.Util.splitByComma
import com.example.csvparser.Util.trimQuotes
import kotlinx.coroutines.withTimeout

private const val TAG = "+++"

class MainRepositoryImpl (val api: Api): MainRepository {

    private var _csvLines = MutableLiveData<List<CsvLine>>()
    override val csvLines: LiveData<List<CsvLine>>
        get() = _csvLines

    override suspend fun fetchData() {
        try {
            val result = withTimeout(10_000) {
                api.fetchCsvData()
            }
            if (result.isSuccessful) {

                val list = mutableListOf<CsvLine>()
                lateinit var titles: List<String>
                var first = true

                // TODO make it streaming, use readLine() in blocking loop
                result.body()?.byteStream()?.bufferedReader()?.readLines()?.forEach {
                    if(first) {
                        first = false
                        titles = parseCsvHeaders(it)
                    } else {
                        list.add(CsvLine(it, titles))
                    }
                }
                _csvLines.value = list
            } else {
                Log.e(TAG, "ERR NO data")
            }

        } catch (error: Throwable) {
            Log.e(TAG,"THROWN:"+error.toString() + " mess:" + error.message)
            throw DataRefreshError("Unable to refresh Lines list", error)
        }
    }

    private fun parseCsvHeaders(line: String): List<String> =
        line.splitByComma().map { it.trimQuotes() }


}

class DataRefreshError(message: String, cause: Throwable) : Throwable(message, cause)


