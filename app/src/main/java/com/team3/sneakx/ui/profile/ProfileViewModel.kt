package com.team3.sneakx.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.local.entity.UserEntity
import com.team3.sneakx.data.repo.ProfileRepository
import com.team3.sneakx.data.repo.ProfileUpdateResult
import com.team3.sneakx.data.session.SessionStore
import com.team3.sneakx.domain.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val error: String? = null,
    val saved: Boolean = false
)

class ProfileViewModel(
    userId: String,
    private val profileRepository: ProfileRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    private val userFlow = profileRepository.observeUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val user: StateFlow<UserEntity?> = userFlow

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            val u = userFlow.filterNotNull().first()
            _ui.value = ProfileUiState(name = u.name, email = u.email)
        }
    }

    fun setName(v: String) {
        _ui.value = _ui.value.copy(name = v, error = null, saved = false)
    }

    fun setEmail(v: String) {
        _ui.value = _ui.value.copy(email = v, error = null, saved = false)
    }

    fun save() {
        viewModelScope.launch {
            val u = userFlow.value ?: return@launch
            val s = _ui.value
            when (val r = profileRepository.updateProfile(u, s.name, s.email)) {
                ProfileUpdateResult.Success -> {
                    sessionStore.setSession(u.id, s.email.trim().lowercase(), Role.fromString(u.role))
                    _ui.value = s.copy(saved = true, error = null)
                }
                is ProfileUpdateResult.Error -> {
                    _ui.value = s.copy(error = r.message, saved = false)
                }
            }
        }
    }
}
