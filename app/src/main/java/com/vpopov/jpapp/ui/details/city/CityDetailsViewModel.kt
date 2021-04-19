package com.vpopov.jpapp.ui.details.city

import com.vpopov.jpapp.model.City
import com.vpopov.jpapp.repository.DetailsRepository
import com.vpopov.jpapp.ui.details.DetailsViewModel

class CityDetailsViewModel(
    detailsRepository: DetailsRepository,
    name: String
) : DetailsViewModel<City>(detailsRepository, name) {
}