package com.vpopov.jpapp.ui.details

import androidx.lifecycle.ViewModel
import com.vpopov.jpapp.repository.DetailsRepository


abstract class DetailsViewModel<Item> constructor(
    protected val detailsRepository: DetailsRepository,
    protected val name: String
) : ViewModel()