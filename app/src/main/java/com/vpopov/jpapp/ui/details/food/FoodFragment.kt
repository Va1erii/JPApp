package com.vpopov.jpapp.ui.details.food

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.FoodFragmentBinding
import com.vpopov.jpapp.extension.setToolbarConfiguration
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration.ImageToolbarConfiguration
import com.vpopov.jpapp.ui.toolbar.ToolbarConfiguration.TitleToolbarConfiguration
import com.vpopov.jpapp.util.GlideRequestListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FoodFragment : Fragment(R.layout.food_fragment) {
    @Inject
    lateinit var assistedFactory: FoodViewModel.Factory

    private val viewModel: FoodViewModel by viewModels {
        FoodViewModel.provideViewModelFactory(
            assistedFactory,
            FoodFragmentArgs.fromBundle(requireArguments()).name
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        val binding = FoodFragmentBinding.bind(view)
        viewModel.food.observe(viewLifecycleOwner) {
            prepareToolbar(it.name, it.image)
        }
    }

    private fun prepareToolbar(name: String, imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .dontAnimate()
            .listener(GlideRequestListener(
                handler = Handler(Looper.getMainLooper()),
                onLoadFailed = { setToolbarConfiguration(TitleToolbarConfiguration(name)) },
                onLoadSuccess = { setToolbarConfiguration(ImageToolbarConfiguration(name, it)) }
            ))
            .submit()
    }
}