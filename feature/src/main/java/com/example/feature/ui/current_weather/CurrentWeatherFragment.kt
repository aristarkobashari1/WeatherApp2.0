package com.example.feature.ui.current_weather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.carousel
import com.example.common.Geocode
import com.example.common.Units
import com.example.common.makeToastShort
import com.example.data.model.mapToWeatherEntity
import com.example.database.entity.Weather
import com.example.feature.currentWeatherWeekForecast
import com.example.feature.databinding.FragmentCurrentWeatherBinding
import com.example.feature.epoxy.modelToEpoxy
import com.example.feature.maps.GeoCodeHelpers
import com.example.feature.util.observeNavigation
import com.example.model.Coord
import com.example.model.PreferenceModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private lateinit var viewBinding: FragmentCurrentWeatherBinding
    private val viewModel: CurrentWeatherViewModel by activityViewModels()
    private val args: CurrentWeatherFragmentArgs by navArgs()
    private lateinit var fusedClientLocation: FusedLocationProviderClient
    private lateinit var geocoder: GeoCodeHelpers

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCurrentWeatherBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation(viewModel)

        fusedClientLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = GeoCodeHelpers(requireActivity(), fusedClientLocation) { cityName,coords->
            viewModel.setDefaultCity(cityName, Coord(coords.lat,coords.lon))
        }

        initFlows()

        if (comingFromWeatherList())
            viewModel.locationData.update { Coord(args.latitude.toDouble(),args.longitude.toDouble()) }
    }

    private fun initFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch { viewModel.homeState.collectLatest { uiState ->
                    when (val currentUiState = uiState.currentWeatherUIState) {
                        is CurrentWeatherUIState.Error -> requireContext().makeToastShort(currentUiState.throwable.toString())
                        CurrentWeatherUIState.Loading -> {}
                        is CurrentWeatherUIState.Success -> viewBinding.weather = currentUiState.data
                    }

                    when (val hourlyUiState = uiState.hourlyWeatherUiState) {
                        is HourlyWeatherUiState.Error -> requireContext().makeToastShort(hourlyUiState.throwable.toString())
                        HourlyWeatherUiState.Loading -> {}
                        is HourlyWeatherUiState.Success -> setUpHourlyRecyclerView(hourlyUiState.data.list.sortedBy { id }.mapToWeatherEntity())
                    }

                    when (val weeklyUiState = uiState.weeklyWeatherUiState) {
                        is WeeklyWeatherUiState.Error -> requireContext().makeToastShort(weeklyUiState.throwable.toString())
                        WeeklyWeatherUiState.Loading -> {}
                        is WeeklyWeatherUiState.Success -> {setUpWeeklyRecyclerView(weeklyUiState.data)}
                    }
                } }

                launch {
                    viewModel.dataStoreDefaultCity.collectLatest { defaultCity->
                        if(defaultCity.second!=Coord(-1.0,-1.0))
                            viewModel.locationData.update { defaultCity.second }
                        else
                            initLocation()
                }
                }

                launch { viewModel.preferences.collectLatest {
                    if(it ==PreferenceModel()) viewModel.setDefaultPreferences()
                    if(it.unit.isNotEmpty()) {
                        when (it.unit){
                            Units.METRIC.value -> viewBinding.apply {
                                    tempUnit = Units.CELCIUS.value
                                    speedUnit = Units.METRE.value
                            }
                            Units.IMPERIAL.value -> viewBinding.apply {
                                tempUnit = Units.FAHRENHEIT.value
                                speedUnit = Units.MILES.value
                            }
                            Units.KELVIN.value -> viewBinding.apply {
                                tempUnit = Units.KELVIN.value
                                speedUnit = Units.METRE.value
                            }

                        }
                    }

                }
                }
            }
        }

    }

    private fun comingFromWeatherList() = args.latitude.toDouble()!=0.0 && args.longitude.toDouble()!=0.0

    private fun setUpHourlyRecyclerView(list: List<Weather>) {
        val hourlyRV = viewBinding.todayWeatherRv
        hourlyRV.layoutManager = LinearLayoutManager(requireContext())
        hourlyRV.withModels {
            carousel {
                id(UUID.randomUUID().toString())
                models(list.modelToEpoxy(viewBinding.tempUnit?:""))
            }
        }
    }

    private fun setUpWeeklyRecyclerView(list: List<Weather>){
        val weeklyRV = viewBinding.forecast7DaysRv
        weeklyRV.layoutManager = LinearLayoutManager(requireContext())
        weeklyRV.withModels {
            list.forEach {
                this.currentWeatherWeekForecast {
                    id(UUID.randomUUID().toString())
                    weekWeather(it)
                    tempUnit(viewBinding.tempUnit)
                    clickListener(View.OnClickListener {
                        requireContext().makeToastShort("click")
                    })
                }
            }


        }
    }

    private fun initLocation(){
        if (geocoder.isLocationPermissionGranted())
            geocoder.requestSingleLocation()
        else
            geocoder.requestLocationPermission(this)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Geocode.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    geocoder.requestSingleLocation()
                 else {
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
                    viewModel.setDefaultCity("GLOBE",Coord(0.0,0.0))
                }
            }
        }
    }
}

