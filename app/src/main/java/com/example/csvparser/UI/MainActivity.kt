package com.example.csvparser.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csvparser.UI.Adapters.LinesAdapter
import com.example.csvparser.Data.MainRepositoryImpl
import com.example.csvparser.Data.getNetworkService
import com.example.csvparser.R
import com.example.csvparser.Util.CustomDelimiter
import com.example.csvparser.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels(
        factoryProducer = { MainViewModel.FACTORY(MainRepositoryImpl(getNetworkService())) }
    )
    private lateinit var binding: ActivityMainBinding
    private val adapter = LinesAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.list.adapter = adapter
        binding.list.setLayoutManager(LinearLayoutManager(this))
        val dividerItemDecoration = CustomDelimiter(this, R.drawable.list_divider)
        binding.list.addItemDecoration(dividerItemDecoration)

        viewModel.lines.observe(this) { value ->
            value?.let {
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        viewModel.snackbar.observe(this) { text ->
            text?.let {
                Snackbar.make(binding.rootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        viewModel.retrieveData()
    }


}
