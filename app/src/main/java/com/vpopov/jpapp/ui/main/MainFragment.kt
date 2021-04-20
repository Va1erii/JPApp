package com.vpopov.jpapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.MainFragmentBinding
import com.vpopov.jpapp.ui.adapter.CityAdapter
import com.vpopov.jpapp.ui.adapter.decoration.VerticalMarginDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        val cityAdapter = CityAdapter()
        binding.cities.adapter = cityAdapter
        binding.cities.addItemDecoration(VerticalMarginDecoration((8 * resources.displayMetrics.density).toInt()))

        lifecycle.addObserver(viewModel)
        viewModel.foods.observe(viewLifecycleOwner) {
        }
        viewModel.cities.observe(viewLifecycleOwner) {
            cityAdapter.update(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
        }
        viewModel.error.observe(viewLifecycleOwner) {
        }

    }
}