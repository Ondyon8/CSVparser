package com.example.csvparser.Data

/**
 * layer between Api serivce and viewModel
 * also contains data logic for parsing data
 */

import androidx.lifecycle.LiveData
import com.example.csvparser.Data.Models.CsvLine

interface MainRepository {
    val csvLines: LiveData<List<CsvLine>>
    suspend fun fetchData()
}