package com.vpopov.jpapp.ui.details.food

import com.vpopov.jpapp.model.Food
import com.vpopov.jpapp.repository.DetailsRepository
import com.vpopov.jpapp.ui.details.DetailsViewModel

class FoodDetailsViewModel(
    detailsRepository: DetailsRepository,
    name: String
) : DetailsViewModel<Food>(detailsRepository, name) {
}