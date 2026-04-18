package com.team3.sneakx.ui.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.repo.CartLineUi
import com.team3.sneakx.data.repo.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    buyerId: String,
    private val cartRepository: CartRepository
) : ViewModel() {
    val lines: StateFlow<List<CartLineUi>> = cartRepository.observeCartLines(buyerId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQty(listingId: String, qty: Int) {
        viewModelScope.launch {
            cartRepository.setQuantity(buyerId, listingId, qty)
        }
    }

    fun remove(listingId: String) {
        viewModelScope.launch {
            cartRepository.removeLine(buyerId, listingId)
        }
    }
}
