package com.team3.sneakx.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.repo.AdminRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminListingsViewModel(
    private val adminUserId: String,
    private val adminRepository: AdminRepository
) : ViewModel() {
    val listings: StateFlow<List<ListingEntity>> =
        adminRepository.observeListings()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeListing(listing: ListingEntity) {
        viewModelScope.launch {
            adminRepository.removeListing(adminUserId, listing)
        }
    }

    fun disableListing(listing: ListingEntity) {
        viewModelScope.launch {
            adminRepository.disableListing(adminUserId, listing)
        }
    }
}
