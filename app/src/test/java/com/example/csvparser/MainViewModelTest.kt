package com.example.csvparser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.csvparser.Data.Api
import com.example.csvparser.Data.MainRepository
import com.example.csvparser.Data.MainRepositoryImpl
import com.example.csvparser.Data.Models.CsvDataType
import com.example.csvparser.Data.Models.CsvLine
import com.example.csvparser.UI.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

@RunWith(JUnit4::class)
class MainViewModelTests {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var api: Api
    lateinit var repository: MainRepository
    lateinit var viewModel: MainViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val testDispatcher = TestCoroutineDispatcher()
        val testScope = TestCoroutineScope(testDispatcher)

        api = Mockito.mock(Api::class.java)     // use mocked
        repository = MainRepositoryImpl(api)    // use real
        viewModel = MainViewModel(repository, testScope)  // use real
    }


    @Test
    fun testApiFetchData() = runBlocking {
        val mockResponseString = "ABC"
        val mockData: ResponseBody = ResponseBody.create(MediaType.parse("text/csv"), mockResponseString)
        val mockResponse: Response<ResponseBody> = Response.success(mockData)

        `when`(api.fetchCsvData()).thenReturn(mockResponse)

        assertEquals(mockResponseString, api.fetchCsvData().body()?.string())
    }

    @Test
    fun testRepoFetchData() = runBlocking {
        val mockResponseString = "Tile1,Title2\r\nValue1,Value2"
        val expectedList = listOf(
            CsvLine(
                values = listOf("Value1","Value2"),
                titles = listOf("Title1","Title2"),
                types = listOf(CsvDataType.GENERAL, CsvDataType.GENERAL)
            )
        )

        val mockData: ResponseBody = ResponseBody.create(MediaType.parse("text/csv"), mockResponseString)
        val mockResponse: Response<ResponseBody> = Response.success(mockData)

        `when`(api.fetchCsvData()).thenReturn(mockResponse)

        repository.fetchData()

        assertEquals(expectedList, repository.csvLines.value)
    }

    @Test
    fun testViewModelFetchData() = runBlocking {
        val mockResponseString = "Tile1,Title2\r\nValue1,Value2"
        val expectedList = listOf(
            CsvLine(
                values = listOf("Value1","Value2"),
                titles = listOf("Tile1","Title2"),
                types = listOf(CsvDataType.GENERAL, CsvDataType.GENERAL)
            )
        )
        val mockData: ResponseBody = ResponseBody.create(MediaType.parse("text/csv"), mockResponseString)
        val mockResponse: Response<ResponseBody> = Response.success(mockData)

        `when`(api.fetchCsvData()).thenReturn(mockResponse)

        viewModel.retrieveData()

        assertEquals(expectedList, viewModel.lines.value)
        assertNull(viewModel.snackbar.value)
        assertEquals(false, viewModel.spinner.value)
    }

}