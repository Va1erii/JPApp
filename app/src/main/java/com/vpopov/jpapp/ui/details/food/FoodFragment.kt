package com.vpopov.jpapp.ui.details.food

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.vpopov.jpapp.R
import com.vpopov.jpapp.databinding.FoodFragmentBinding
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
        setSharedElementTransitionOnEnter()
        postponeEnterTransition()

        val binding = FoodFragmentBinding.bind(view)

        viewModel.food.observe(viewLifecycleOwner) {
            binding.image.apply {
                transitionName = it.image
                startEnterTransitionAfterLoadingImage(it.image, this)
            }
        }
    }

    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }

    private fun startEnterTransitionAfterLoadingImage(
        imageUrl: String,
        imageView: ImageView
    ) {
        Glide.with(this)
            .load(imageUrl)
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(imageView)
    }
}