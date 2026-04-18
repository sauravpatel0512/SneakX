package com.team3.sneakx.ui.buyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3.sneakx.data.repo.CheckoutResult
import com.team3.sneakx.data.repo.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val shipping: String = "",
    val paymentMethod: String = "Mock card",
    val loading: Boolean = false,
    val error: String? = null,
    val successOrderId: String? = null,
    val paymentFailedMessage: String? = null
)

class CheckoutViewModel(
    private val buyerId: String,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(CheckoutUiState())
    val ui: StateFlow<CheckoutUiState> = _ui.asStateFlow()

    fun setShipping(v: String) {
        _ui.value = _ui.value.copy(shipping = v, error = null)
    }

    fun setPaymentMethod(v: String) {
        _ui.value = _ui.value.copy(paymentMethod = v, error = null)
    }

    fun placeOrder() {
        viewModelScope.launch {
            val s = _ui.value
            if (s.shipping.isBlank()) {
                _ui.value = s.copy(error = "Enter shipping information.")
                return@launch
            }
            if (s.paymentMethod.isBlank()) {
                _ui.value = s.copy(error = "Select a payment method.")
                return@launch
            }
            _ui.value = s.copy(loading = true, error = null, paymentFailedMessage = null)
            when (val r = orderRepository.checkout(buyerId, s.shipping, s.paymentMethod)) {
                is CheckoutResult.Success -> {
                    _ui.value = s.copy(loading = false, successOrderId = r.orderId)
                }
                is CheckoutResult.Error -> {
                    _ui.value = s.copy(loading = false, error = r.message)
                }
                is CheckoutResult.PaymentFailed -> {
                    _ui.value = s.copy(loading = false, paymentFailedMessage = r.message)
                }
            }
        }
    }
}
