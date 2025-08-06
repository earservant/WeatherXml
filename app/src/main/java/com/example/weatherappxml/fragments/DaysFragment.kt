package com.example.weatherappxml.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappxml.MainUiState
import com.example.weatherappxml.MainViewModel
import com.example.weatherappxml.R
import com.example.weatherappxml.adapters.DaysAdapter
import com.example.weatherappxml.adapters.ForecastDay
import com.example.weatherappxml.databinding.FragmentDaysBinding

class DaysFragment : Fragment(), DaysAdapter.Listener {

    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: DaysAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        initRecyclerView()
        observeWeatherData()
    }

    private fun initRecyclerView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = DaysAdapter(this@DaysFragment)
        rcView.adapter = adapter
    }

    private fun observeWeatherData() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainUiState.Data -> {
                    val dailyData = state.data.forecast?.forecastDays
                    if (dailyData != null) {
                        adapter.submitList(dailyData)
                    } else {
                        Toast.makeText(
                            activity,
                            getString(R.string.no_daily_available),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                is MainUiState.Error -> {
                    state.errorMessage
                }

                MainUiState.Loading -> {
                }
            }
        }
    }

    override fun onClick(item: ForecastDay) {
        viewModel.liveDataSelectedDay.value = item
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}