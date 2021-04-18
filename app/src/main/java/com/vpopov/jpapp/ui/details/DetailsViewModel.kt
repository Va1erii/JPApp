package com.vpopov.jpapp.ui.details

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vpopov.jpapp.repository.DetailsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize


class DetailsViewModel @AssistedInject constructor(
    private val detailsRepository: DetailsRepository,
    @Assisted private val item: Item
) {


    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(item: Item): DetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            item: Item
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(item) as T
            }
        }
    }
}

data class Item(
    val name: String,
    val type: ItemType
)

sealed class ItemType : Parcelable {
    @Parcelize
    object City : ItemType()

    @Parcelize
    object Food : ItemType()
}