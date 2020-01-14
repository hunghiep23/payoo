package com.hiep.payootest.feature.topup.confirminfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OTPViewModel : ViewModel() {
    val otpLiveData = MutableLiveData<Boolean>().apply { value = false }

    fun sendOTP(otp: String) {
        otpLiveData.value = true
    }
}