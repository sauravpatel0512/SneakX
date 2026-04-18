package com.team3.sneakx.ui.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.repo.CartRepository
import com.team3.sneakx.data.repo.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListingDetailViewModel(
    private val listingId: String,
    private val listingRepository: ListingRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _listing = MutableStateFlow<ListingEntity?>(null)
    val listing: StateFlow<ListingEntity?> = _listing.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            _listing.value = listingRepository.getListing(listingId)
        }
    }

    fun addToCart(buyerId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            cartRepository.addToCart(buyerId, listingId, 1)
            _message.value = "Added to cart"
            onDone()
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
