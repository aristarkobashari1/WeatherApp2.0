package com.example.feature.ui.weather_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.makeToastShort
import com.example.model.Coord
import com.example.feature.util.observeNavigation
import com.example.database.entity.Weather
import com.example.database.entity.nothingFound
import com.example.feature.city
import com.example.feature.databinding.FragmentWeatherListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class WeatherListFragment : Fragment() {

    private lateinit var viewBinding: FragmentWeatherListBinding
    private val viewModel: WeatherListViewModel by activityViewModels()

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

        observeNavigation(viewModel)
        initFlows()
        searchCity()
    }

    private fun initFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.cityResult.collectLatest {
                        if(it==null) requireContext().makeToastShort("Nothing found")
                    }
                }
                launch{ viewModel.weatherListDB.collectLatest { list ->
                        if (list.isNotEmpty()) setUpCityAdapter(list)
                    }
                }
            }
        }
    }


    private fun searchCity() {
        viewBinding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onSearchTextChange(viewBinding.searchEditText.text.toString())
                true
            } else false
        }
    }

    private fun setUpCityAdapter(list: List<Weather>) {
        val citiesRv = viewBinding.rvCities
        citiesRv.layoutManager = LinearLayoutManager(requireContext())
        citiesRv.withModels {
            list.forEach { weather ->
                this.city {
                    id(UUID.randomUUID().toString())
                    weather(weather)
                    clickListener(View.OnClickListener {
                        viewModel.displaySelectedWeather(
                            Coord(weather.latitude!!.toDouble(), weather.longitude!!.toDouble()))
                    })
                    longClickListener(View.OnLongClickListener {
                        setUpDialog(Coord(weather.latitude!!.toDouble(), weather.longitude!!.toDouble()))
                        true
                    })
                }
            }
        }
    }

    //move to helper module
    private fun setUpDialog(coord: Coord) = MaterialAlertDialogBuilder(requireContext())
        .setTitle("Attention")
        .setMessage("Do you want to set this location as default?")
        .setPositiveButton("Yes"){ _, _ -> viewModel.setDefaultCity(coord)}
        .setNegativeButton("No"){ dialog, _ -> dialog.dismiss()}
        .show()
}