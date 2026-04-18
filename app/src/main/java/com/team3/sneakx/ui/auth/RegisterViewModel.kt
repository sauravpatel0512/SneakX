package com.team3.sneakx.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.repo.AuthRepository
import com.team3.sneakx.data.repo.RegisterResult
import com.team3.sneakx.domain.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: Role = Role.BUYER,
    val error: String? = null,
    val loading: Boolean = false
)

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(RegisterUiState())
    val ui: StateFlow<RegisterUiState> = _ui.asStateFlow()

    fun setName(v: String) {
        _ui.value = _ui.value.copy(name = v, error = null)
    }

    fun setEmail(v: String) {
        _ui.value = _ui.value.copy(email = v, error = null)
    }

    fun setPassword(v: String) {
        _ui.value = _ui.value.copy(password = v, error = null)
    }

    fun setRole(r: Role) {
        _ui.value = _ui.value.copy(role = r, error = null)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val s = _ui.value
            _ui.value = s.copy(loading = true, error = null)
            when (val res = authRepository.register(s.name, s.email, s.password, s.role)) {
                RegisterResult.Success -> {
                    _ui.value = s.copy(loading = false)
                    onSuccess()
                }
                is RegisterResult.Error -> {
                    _ui.value = s.copy(loading = false, error = res.message)
                }
            }
        }
    }
}
