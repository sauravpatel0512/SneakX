package com.team3.sneakx.ui.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.data.repo.ListingRepository
import com.team3.sneakx.domain.ListingSort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class BrowseFilter(
    val keyword: String = "",
    val category: String? = null,
    val minPrice: String = "",
    val maxPrice: String = "",
    val sort: ListingSort = ListingSort.NEWEST
)

class BrowseViewModel(
    private val listingRepository: ListingRepository
) : ViewModel() {
    private val _filter = MutableStateFlow(BrowseFilter())
    val filter: StateFlow<BrowseFilter> = _filter.asStateFlow()

    val listings: StateFlow<List<ListingEntity>> = _filter
        .flatMapLatest { f ->
            val min = f.minPrice.toDoubleOrNull()
            val max = f.maxPrice.toDoubleOrNull()
            listingRepository.observeMarketplaceListings(
                keyword = f.keyword,
                category = f.category?.takeIf { it.isNotBlank() },
                minPrice = min,
                maxPrice = max,
                sort = f.sort
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setKeyword(v: String) {
        _filter.update { it.copy(keyword = v) }
    }

    fun setCategory(v: String?) {
        _filter.update { it.copy(category = v) }
    }

    fun setMinPrice(v: String) {
        _filter.update { it.copy(minPrice = v) }
    }

    fun setMaxPrice(v: String) {
        _filter.update { it.copy(maxPrice = v) }
    }

    fun setSort(s: ListingSort) {
        _filter.update { it.copy(sort = s) }
    }

    fun applySearch() {
        _filter.update { it.copy() }
    }
}
