package com.vpopov.jpapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.MainFragmentBinding
import com.vpopov.jpapp.extension.setToolbarConfiguration
import com.vpopov.jpapp.ui.adapter.CityAdapter
import com.vpopov.jpapp.ui.adapter.FoodAdapter
import com.vpopov.jpapp.ui.adapter.decoration.VerticalMarginDecoration
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration.TitleToolbarConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        setToolbarConfiguration(TitleToolbarConfiguration("Foods and Cities"))
        val cityAdapter = CityAdapter { city ->
            findNavController().navigate(MainFragmentDirections.openCityDetails(city.name))
        }
        val foodAdapter = FoodAdapter { food ->
            findNavController().navigate(MainFragmentDirections.openFoodDetails(food.name))
        }
        binding.cities.addItemDecoration(
            VerticalMarginDecoration(
                resources.getDimension(R.dimen.recycler_view_item_vertical_margin).toInt()
            )
        )
        val citiesSkeleton = prepareSkeletonLoading(
            binding.cities,
            cityAdapter,
            R.layout.skeletong_city_item_view
        )
        binding.foods.addItemDecoration(
            VerticalMarginDecoration(
                resources.getDimension(R.dimen.recycler_view_item_vertical_margin).toInt()
            )
        )
        val foodsSkeleton = prepareSkeletonLoading(
            binding.foods,
            foodAdapter,
            R.layout.skeleton_food_item_view
        )
        lifecycle.addObserver(viewModel)
        viewModel.foods.observe(viewLifecycleOwner) {
            foodsSkeleton.hide()
            foodAdapter.update(it)
        }
        viewModel.cities.observe(viewLifecycleOwner) {
            citiesSkeleton.hide()
            cityAdapter.update(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
        }
    }

    private fun prepareSkeletonLoading(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        @LayoutRes skeletonItemRes: Int
    ): SkeletonScreen {
        return Skeleton.bind(recyclerView)
            .adapter(adapter)
            .angle(20)
            .count(3)
            .color(R.color.white)
            .shimmer(true)
            .load(skeletonItemRes)
            .show()
    }
}