package com.vpopov.jpapp.ui.details.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vpopov.jpapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityFragment : Fragment(R.layout.food_fragment) {
    @Inject
    lateinit var assistedFactory: CityViewModel.Factory

    private val viewModel: CityViewModel by viewModels {
        CityViewModel.provideViewModelFactory(
            assistedFactory,
            CityFragmentArgs.fromBundle(requireArguments()).name
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel
    }
}