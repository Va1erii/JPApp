package com.vpopov.jpapp.ui.details.city

import android.os.Bundle
import android.view.View
import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.ui.details.DetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityDetailsFragment : DetailsFragment<City>() {
    private val viewModel: CityDetailsViewModel by provideViewModel(
        CityDetailsFragmentArgs.fromBundle(requireArguments()).name
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }
}