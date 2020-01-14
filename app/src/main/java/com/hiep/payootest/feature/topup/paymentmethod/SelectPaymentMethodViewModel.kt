package com.hiep.payootest.feature.topup.paymentmethod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiep.payootest.PaymentMethod
import com.hiep.payootest.model.TopUpParams

class SelectPaymentMethodViewModel : ViewModel() {
    private var _paymentMethod: PaymentMethod? = null
    private var _isValidPaymentMethod: Boolean = false
    val allowNextStepLiveData = MutableLiveData<Boolean>().apply { value = _isValidPaymentMethod }
    val isEnabledMethodLiveData = MutableLiveData<HashMap<PaymentMethod, Boolean>>()

    var paymentMethod: PaymentMethod?
        get() = _paymentMethod
        set(value) {
            _paymentMethod = value
            _isValidPaymentMethod = _paymentMethod != null
            allowNextStepLiveData.value = _isValidPaymentMethod
        }


    fun applyRules(topUpParams: TopUpParams?) {
        val enableMethodMap = HashMap<PaymentMethod, Boolean>()
        if (topUpParams?.receiverPhone != null)
            enableMethodMap.putAll(phoneRule(topUpParams.receiverPhone))
        if (topUpParams?.amount != null)
            enableMethodMap.putAll(amountRule(topUpParams.amount))
        isEnabledMethodLiveData.value = enableMethodMap
    }

    private fun phoneRule(phone: String): HashMap<PaymentMethod, Boolean> {
        val disablePrefixes = arrayOf("090")
        val phonePrefix = phone.substring(0, 3)
        var enable = true
        disablePrefixes.forEach { prefix ->
            if (prefix == phonePrefix) {
                enable = false
                return@forEach
            }
        }
        return HashMap<PaymentMethod, Boolean>().apply {
            put(PaymentMethod.LinkedAccount, enable)
        }
    }

    private fun amountRule(amount: Long): HashMap<PaymentMethod, Boolean> {
        val minimumAmount = 100000
        val enable = amount >= minimumAmount
        return HashMap<PaymentMethod, Boolean>().apply {
            put(PaymentMethod.DomesticCard, enable)
            put(PaymentMethod.InternationalCard, enable)
        }
    }

}

