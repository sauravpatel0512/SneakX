package com.team3.sneakx.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.repo.ListingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SellerListingsViewModel(
    sellerId: String,
    private val listingRepository: ListingRepository
) : ViewModel() {
    val listings: StateFlow<List<ListingEntity>> =
        listingRepository.observeSellerListings(sellerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(listing: ListingEntity) {
        viewModelScope.launch {
            listingRepository.deleteListing(listing)
        }
    }
}
