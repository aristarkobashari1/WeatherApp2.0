package com.example.feature.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.feature.databinding.FragmentProfileBinding
import com.example.feature.util.observeNavigation
import com.example.model.PreferenceModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Profile : Fragment() {

    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(layoutInflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeNavigation(viewModel)
        initFlow()
    }

    private fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profileData.collectLatest {
                        if(it!= PreferenceModel())
                            viewBinding.apply {
                                location = it.location
                                unit = it.unit
                                language = it.language
                            }
                    }
                }
            }
        }
    }
}