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
import com.example.weatherappxml.adapters.Condition
import com.example.weatherappxml.adapters.HourlyAdapter
import com.example.weatherappxml.adapters.HourlyWeather
import com.example.weatherappxml.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: HourlyAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        initRcView()
        observeWeatherData()
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = HourlyAdapter(null)
        rcView.adapter = adapter
    }

    private fun observeWeatherData() {
        //Наблюдение за текущими данными (начало сессии)
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainUiState.Data -> {
                    val hourlyData =
                        state.data.forecast?.forecastDays?.firstOrNull()?.hour?.map { hourly ->
                            HourlyWeather(
                                time = hourly.time,
                                tempC = hourly.tempC.toDouble().toInt().toString(),
                                condition = Condition(
                                    text = hourly.condition.text,
                                    icon = hourly.condition.icon
                                )
                            )
                        }
                    if (hourlyData != null) {
                        adapter.submitList(hourlyData)
                    } else {
                        Toast.makeText(activity,
                            getString(R.string.no_hourly_weather_data_available), Toast.LENGTH_SHORT)
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

        viewModel.liveDataSelectedDay.observe(viewLifecycleOwner) { selectedDay ->
            val hourlyData = selectedDay.hour.map { hourly ->
                HourlyWeather(
                    time = hourly.time,
                    tempC = hourly.tempC.toDouble().toInt().toString(),
                    condition = Condition(
                        text = hourly.condition.text,
                        icon = hourly.condition.icon
                    )
                )
            }
            adapter.submitList(hourlyData)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}