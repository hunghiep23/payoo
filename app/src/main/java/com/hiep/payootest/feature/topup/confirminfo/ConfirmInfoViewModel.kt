package com.hiep.payootest.feature.topup.confirminfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hiep.payootest.data.Repositories
import com.hiep.payootest.model.Response
import com.hiep.payootest.model.TopUpParams
import com.hiep.payootest.model.json.TransferBody
import com.hiep.payootest.model.json.TransferResponseJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class ConfirmInfoViewModel : ViewModel() {
    private val repositories = Repositories()
    private var disposable = Disposables.empty()
    private val _maxOtpCount = 5
    private var _otpCount = 0
    private var _isValidInfo = false
    val transfertLiveData = MutableLiveData<Response<TransferResponseJson>>()
    val allowSendOTPLiveData =
        MutableLiveData<Boolean>().apply { value = _isValidInfo && _otpCount < _maxOtpCount }

    var otpCount: Int
        get() = _otpCount
        set(value) {
            _otpCount = value
            if (_otpCount >= _maxOtpCount)
                allowSendOTPLiveData.value = false
            else {
                allowSendOTPLiveData.value = _isValidInfo
            }
        }

    fun confirmInfo(topUpParams: TopUpParams?) {
        if (topUpParams?.senderPhone != null && topUpParams.receiverPhone != null && topUpParams.amount != null && topUpParams.paymentMethod != null) {
            _isValidInfo = true
            allowSendOTPLiveData.value = _isValidInfo && _otpCount < _maxOtpCount
        }
    }

    fun transfer(topUpParams: TopUpParams) {
        val transferBody = TransferBody(
            topUpParams.senderPhone,
            topUpParams.receiverPhone,
            topUpParams.amount,
            topUpParams.paymentMethod.toString()
        )
        disposable.dispose()
        disposable = repositories.transfer(transferBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                transfertLiveData.value = Response.loading()
            }
            .subscribe({
                transfertLiveData.value = Response.succeed(it)
            }, {
                //do something
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}