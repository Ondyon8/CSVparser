package com.example.csvparser.UI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csvparser.Data.DataRefreshError
import com.example.csvparser.Data.MainRepository
import com.example.csvparser.singleArgViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainViewModel (
    private val repository: MainRepository,
    private val scope: CoroutineScope? = null   // use this scope for testing, use viewModelScope otherwise
) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }
    val lines = repository.csvLines

    private val _snackBar = MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get() = _snackBar

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner


    fun onSnackbarShown() {
        _snackBar.value = null
    }

    fun retrieveData() {
        (scope ?: viewModelScope).launch {
            try {
                _spinner.value = true
                repository.fetchData()
            } catch (error: DataRefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}
