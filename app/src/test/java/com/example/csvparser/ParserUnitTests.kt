package com.example.csvparser

import com.example.csvparser.Data.Models.CsvDataType
import com.example.csvparser.Data.Models.CsvLine
import org.junit.Test

import org.junit.Assert.*

class ParserUnitTests {

    @Test
    fun parseCsvValues_correct() {
        val inputString = "ABCD,EFG"
        val titles = listOf("Title1","Title2")
        val obj = CsvLine(inputString, titles)
        assertEquals(listOf("ABCD","EFG"), obj.values)
        assertTrue(obj.isCorrect())
    }

    @Test
    fun parseCsvValues_correctWithQuotes() {
        val inputString = "ABCD,\"EFG\""
        val titles = listOf("\"Title1\"","Title2")
        val obj = CsvLine(inputString, titles)
        assertTrue(obj.isCorrect())
        assertEquals(listOf("ABCD","EFG"), obj.values)
    }

    @Test
    fun parseCsvValues_correctWithCommaInQuotes() {
        val inputString = "ABCD,\"EF,G\""
        val titles = listOf("\"Title1\"","Title2")
        val obj = CsvLine(inputString, titles)
        assertTrue(obj.isCorrect())
        assertEquals(listOf("ABCD","EF,G"), obj.values)
    }

    @Test
    fun parseCsvValues_correctWithEscapedQuotes() {
        val inputString = "ABCD,\"EF\\\"G\""
        val titles = listOf("Title1","Title2")
        val obj = CsvLine(inputString, titles)
        assertEquals(2, obj.values.size)
        assertTrue(obj.isCorrect())
        assertEquals("ABCD", obj.values[0])
        assertEquals(listOf("ABCD","EF\\\"G" ), obj.values)
    }

    @Test
    fun parseCsvValues_incorrect() {
        val inputString = "ABCD,EFG"
        val titles = listOf("Title1")
        val obj = CsvLine(inputString, titles)
        assertFalse(obj.isCorrect())
        assertEquals(listOf("ABCD","EFG"), obj.values)
    }

    @Test
    fun parseCsvValues_types() {
        val inputString = "ABCD,7,8.0,\"2020-05-15T13:30:00\",\"http://google.com\""
        val titles = listOf("Title1","Title2","Titile3","Title4","Title5")
        val obj = CsvLine(inputString, titles)
        assertEquals( 5, obj.values.size)
        assertTrue(obj.isCorrect())
        assertEquals(
            listOf(
                CsvDataType.GENERAL,
                CsvDataType.INT,
                CsvDataType.FLOAT,
                CsvDataType.DATE,
                CsvDataType.LINK
            ),
            obj.types
        )
    }
}