package com.hiep.payootest.feature.topup.enterphonenumber

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiep.payootest.model.FaceValue

class EnterPhoneViewModel : ViewModel() {
    private var _senderPhone: String = "0123456789"
    private var _receiverPhone: String? = null
    private var _amount: Long? = null
    private var _isValidPhone: Boolean = false
    private var _isValidAmount: Boolean = false

    val allowNextStepLiveData =
        MutableLiveData<Boolean>().apply { value = _isValidPhone && _isValidAmount }
    val amountListLiveData = MutableLiveData<List<FaceValue>>().apply {
        value = ArrayList<FaceValue>().apply {
            add(FaceValue(10000, "10.000đ"))
            add(FaceValue(20000, "20.000đ"))
            add(FaceValue(30000, "30.000đ"))
            add(FaceValue(50000, "50.000đ"))
            add(FaceValue(100000, "100.000đ"))
            add(FaceValue(200000, "200.000đ"))
            add(FaceValue(300000, "300.000đ"))
            add(FaceValue(500000, "500.000đ"))
        }
    }
    val senderPhone: String?
        get() = _senderPhone

    var receiverPhone: String?
        get() = _receiverPhone
        set(value) {
            _receiverPhone = value
            _isValidPhone = isValidPhone(value)
            allowNextStepLiveData.value = _isValidPhone && _isValidAmount
        }

    var amount: Long?
        get() = _amount
        set(value) {
            _amount = value
            _isValidAmount = isValidAmount(value)
            allowNextStepLiveData.value = _isValidPhone && _isValidAmount
        }

    private fun isValidPhone(phone: String?) =
        (phone != null && phone.length >= 10 && phone[0] == '0')

    private fun isValidAmount(amount: Long?): Boolean {
        if (amount != null) {
            return amount >= 10000L
        }
        return false
    }
}