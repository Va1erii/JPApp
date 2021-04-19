package com.vpopov.jpapp.ui.details.food

import android.os.Bundle
import android.view.View
import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.ui.details.DetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailsFragment : DetailsFragment<Food>() {
    private val viewModel: FoodDetailsViewModel by provideViewModel("")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}