package com.team3.sneakx.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.repo.AuthRepository
import com.team3.sneakx.data.repo.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val lockedUntil: Long? = null,
    val loading: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui.asStateFlow()

    fun setEmail(v: String) {
        _ui.value = _ui.value.copy(email = v, error = null)
    }

    fun setPassword(v: String) {
        _ui.value = _ui.value.copy(password = v, error = null)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, error = null, lockedUntil = null)
            when (val r = authRepository.login(_ui.value.email, _ui.value.password)) {
                LoginResult.Success -> {
                    _ui.value = _ui.value.copy(loading = false)
                    onSuccess()
                }
                LoginResult.InvalidCredentials -> {
                    _ui.value = _ui.value.copy(loading = false, error = "Invalid email or password.")
                }
                LoginResult.AccountDisabled -> {
                    _ui.value = _ui.value.copy(loading = false, error = "Account is disabled.")
                }
                is LoginResult.Locked -> {
                    _ui.value = _ui.value.copy(loading = false, lockedUntil = r.untilMillis, error = "Too many attempts. Account locked.")
                }
            }
        }
    }
}
