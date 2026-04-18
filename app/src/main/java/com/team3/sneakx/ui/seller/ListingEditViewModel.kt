package com.team3.sneakx.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.repo.ListingRepository
import com.team3.sneakx.util.photosFromJson
import com.team3.sneakx.util.photosToJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListingEditUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val condition: String = "",
    val photoUris: List<String> = emptyList(),
    val error: String? = null,
    val saved: Boolean = false,
    val loading: Boolean = false
)

class ListingEditViewModel(
    private val sellerId: String,
    private val existingListingId: String?,
    private val listingRepository: ListingRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(ListingEditUiState())
    val ui: StateFlow<ListingEditUiState> = _ui.asStateFlow()

    private var existing: ListingEntity? = null

    init {
        if (existingListingId != null) {
            viewModelScope.launch {
                val ex = listingRepository.getListing(existingListingId)
                existing = ex
                if (ex != null && ex.sellerId == sellerId) {
                    _ui.value = ListingEditUiState(
                        title = ex.title,
                        description = ex.description,
                        category = ex.category,
                        price = ex.price.toString(),
                        condition = ex.condition,
                        photoUris = photosFromJson(ex.photosJson)
                    )
                }
            }
        }
    }

    fun setTitle(v: String) {
        _ui.value = _ui.value.copy(title = v, error = null)
    }

    fun setDescription(v: String) {
        _ui.value = _ui.value.copy(description = v, error = null)
    }

    fun setCategory(v: String) {
        _ui.value = _ui.value.copy(category = v, error = null)
    }

    fun setPrice(v: String) {
        _ui.value = _ui.value.copy(price = v, error = null)
    }

    fun setCondition(v: String) {
        _ui.value = _ui.value.copy(condition = v, error = null)
    }

    fun setPhotos(uris: List<String>) {
        _ui.value = _ui.value.copy(photoUris = uris, error = null)
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            val s = _ui.value
            if (s.title.isBlank() || s.description.isBlank() || s.category.isBlank() ||
                s.price.isBlank() || s.condition.isBlank() || s.photoUris.isEmpty()
            ) {
                _ui.value = s.copy(error = "All fields including at least one photo are required.")
                return@launch
            }
            val price = s.price.toDoubleOrNull()
            if (price == null || price < 0) {
                _ui.value = s.copy(error = "Invalid price.")
                return@launch
            }
            _ui.value = s.copy(loading = true, error = null)
            val json = photosToJson(s.photoUris)
            val ex = existing
            if (ex != null) {
                listingRepository.updateListing(ex, s.title, s.description, s.category, price, s.condition, json)
            } else {
                listingRepository.createListing(
                    sellerId, s.title, s.description, s.category, price, s.condition, json
                )
            }
            _ui.value = s.copy(loading = false, saved = true)
            onDone()
        }
    }
}
