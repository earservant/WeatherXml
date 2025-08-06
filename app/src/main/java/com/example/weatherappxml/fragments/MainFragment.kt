package com.example.weatherappxml.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherappxml.MainUiState
import com.example.weatherappxml.MainViewModel
import com.example.weatherappxml.R
import com.example.weatherappxml.adapters.VpAdapter
import com.example.weatherappxml.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {
    private lateinit var fLocationClient: FusedLocationProviderClient
    private val tList = listOf("Hours", "Days")
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeWeatherData()
        requestLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedCity().observe(viewLifecycleOwner) { city ->
            if (city != null) {
                // Используем сохранённый город
                viewModel.fetchWeather(city)
//                Toast .makeText(activity, "Данные из кеша", Toast.LENGTH_SHORT).show()
            } else {
                getLocation()
//                Toast .makeText(activity, "Сработал getLocation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeWeatherData() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MainUiState.Data -> {
                    with(binding) {
                        tvCity.text = state.data.location.name
                        tvData.text = state.data.location.localtime
                        tvCondition.text = state.data.current.condition.text
                        tvCurrentTemp.text = "${state.data.current.tempC.toInt()}°C"
                        tvMaxMin.text =
                            "Min: ${
                                state.data.forecast?.forecastDays?.firstOrNull()?.day?.minTempC?.toInt()
                            }°C " +
                                    "Max: ${state.data.forecast?.forecastDays?.firstOrNull()?.day?.maxTempC?.toInt()}°C"

                        Picasso.get()
                            .load(state.data.current.condition.icon.formatImageUrl())
                            .into(imWeather)
                    }
                }

                is MainUiState.Error -> {
                    state.errorMessage
                }

                is MainUiState.Loading -> {
                    with(binding) {
                        tvCity.text = getString(R.string.loading)
                    }
                }
            }

        }
    }

    private fun init() = with(binding) {
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val adapter = VpAdapter(
            activity as FragmentActivity,
            listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
        )
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp) { tab, position ->
            tab.text = tList[position]
        }.attach()
        ibSync.setOnClickListener {
            getLocation()
        }
        ibSearch.setOnClickListener {
            DialogManager.searchByNameDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(cityName: String) {
                    if (cityName.isNotEmpty()) {
                        viewModel.saveCity(cityName)
                        viewModel.fetchWeather(cityName)
                    }
                }
            })
        }
    }

    private fun getLocation() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) return

        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(requireContext(),
                getString(R.string.location_deactivated), Toast.LENGTH_SHORT).show()
            DialogManager.showLocationSettingsDialog(requireContext()) {}
            return
        }

        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = "${task.result.latitude},${task.result.longitude}"
                    viewModel.saveCity(location)
                    viewModel.fetchWeather(location)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.getlocation_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun requestLocationPermission() {
        pLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//              getLocation()
//            }
            }
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
