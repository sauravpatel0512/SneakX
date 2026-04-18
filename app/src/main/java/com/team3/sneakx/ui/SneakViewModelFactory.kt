package com.team3.sneakx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team3.sneakx.AppContainer
import com.team3.sneakx.ui.auth.LoginViewModel
import com.team3.sneakx.ui.auth.RegisterViewModel
import com.team3.sneakx.ui.buyer.BrowseViewModel

@Suppress("UNCHECKED_CAST")
class SneakViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val c = container
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(c.authRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(c.authRepository) as T
            modelClass.isAssignableFrom(BrowseViewModel::class.java) ->
                BrowseViewModel(c.listingRepository) as T
            else -> throw IllegalArgumentException("Unknown VM ${modelClass.name}")
        }
    }
}
