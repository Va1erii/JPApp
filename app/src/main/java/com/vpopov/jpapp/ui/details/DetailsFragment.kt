package com.vpopov.jpapp.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    companion object {
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val EXTRA_TYPE = "EXTRA_TYPE"
    }

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModel.AssistedFactory

    private val viewModel: DetailsViewModel by viewModels {
        val itemType: ItemType = requireArguments().getParcelable(EXTRA_TYPE)!!
        val name: String = requireArguments().getString(EXTRA_ITEM)!!
        DetailsViewModel.provideFactory(detailsViewModelFactory, Item(name, itemType))
    }


}