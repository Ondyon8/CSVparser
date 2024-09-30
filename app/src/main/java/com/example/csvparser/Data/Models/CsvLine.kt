package com.example.csvparser.Data.Models

/**
 * represent one line of CSV file with values, types and column titles
 * contains methods for
 *  parsing columns from string
 *  guessing the type of the column
 */

import android.annotation.SuppressLint
import com.example.csvparser.Util.splitByComma
import com.example.csvparser.Util.trimQuotes
import java.text.SimpleDateFormat


enum class CsvDataType {
    DATE,
    INT,
    FLOAT,
    LINK,
    GENERAL
}

data class CsvLine(
    val values: List<String>,
    val titles: List<String>,
    var types: List<CsvDataType>
) {
    constructor(
        rawLine: String,
        titles: List<String>,
    ) : this(
        rawLine.splitByComma().map { it.trimQuotes() },
        titles,
        listOf()
    ) {
        // values already split at this point
        this.types = makeTypesList()
    }

    private fun makeTypesList(): List<CsvDataType> = this.values.map { detectType(it) }

    @SuppressLint("SimpleDateFormat")
    private fun detectType(value: String): CsvDataType {
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return CsvDataType.LINK
        }
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //TODO add more patterns if needed
        val dateTime = try {
            formatter.parse(value)
        } catch (e: Exception) {
            null
        }
        if (dateTime != null) {
            return CsvDataType.DATE
        }
        val parsedInt = value.toIntOrNull()
        if (parsedInt != null) {
            return CsvDataType.INT
        }
        val parsedFloat = value.toFloatOrNull()
        if (parsedFloat != null) {
            return CsvDataType.FLOAT
        }
        return CsvDataType.GENERAL
    }

    fun isCorrect() = this.values.size == this.titles.size
}
