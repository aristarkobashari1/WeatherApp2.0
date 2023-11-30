package com.example.feature.ui.weather_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.common.makeToastShort
import com.example.database.entity.Weather
import com.example.feature.city
import com.example.feature.databinding.FragmentWeatherListBinding
import com.example.feature.epoxy.setUpGenericAdapter
import com.example.feature.maps.GeoCodeHelpers
import com.example.feature.util.observeFlows
import com.example.feature.util.observeNavigation
import com.example.feature.util.setUpDialog
import com.example.model.Coord
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import java.util.UUID


class WeatherListFragment : Fragment() {

    private lateinit var viewBinding: FragmentWeatherListBinding
    private val viewModel: WeatherListViewModel by activityViewModels()
    private lateinit var geoCodeHelpers: GeoCodeHelpers

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentWeatherListBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geoCodeHelpers = GeoCodeHelpers(requireActivity())
        observeNavigation(viewModel)
        initFlows()
        searchCity()
    }

    private fun initFlows() = observeFlows({
            viewModel.weatherListDB.collectLatest { list ->
                if (list.isNotEmpty()) setUpCityAdapter(list.sortedByDescending { it.id })
            }
        })


    private fun searchCity() {
        viewBinding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val latLong =
                    geoCodeHelpers.getLocationFromAddress(viewBinding.searchEditText.text.toString())
                if (latLong != null) {
                    viewModel.inputCoords.update { Coord(latLong.lat, latLong.lon) }
                } else {
                    requireContext().makeToastShort("No city found")
                }
                true
            } else false
        }
    }

    private fun setUpCityAdapter(list:List<Weather>) = viewBinding.rvCities.setUpGenericAdapter(list,requireContext()){
            weather, epoxyController -> epoxyController.city {
                id(UUID.randomUUID().toString())
                weather(weather)
                clickListener(View.OnClickListener {
                    viewModel.displaySelectedWeather(Coord(weather.latitude!!.toDouble(), weather.longitude!!.toDouble()))
                })
                longClickListener(View.OnLongClickListener {
                    requireContext().setUpDialog{
                        viewModel.setDefaultCity(Coord(weather.latitude!!.toDouble(), weather.longitude!!.toDouble()))
                    }
                    true
                })
            }
        }
}