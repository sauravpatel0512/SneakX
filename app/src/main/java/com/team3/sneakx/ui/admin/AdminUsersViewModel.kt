package com.team3.sneakx.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.UserEntity
import com.team3.sneakx.data.repo.AdminRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminUsersViewModel(
    private val adminUserId: String,
    private val adminRepository: AdminRepository
) : ViewModel() {
    val users: StateFlow<List<UserEntity>> =
        adminRepository.observeUsers()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setEnabled(user: UserEntity, enabled: Boolean) {
        viewModelScope.launch {
            adminRepository.setUserEnabled(adminUserId, user, enabled)
        }
    }
}
