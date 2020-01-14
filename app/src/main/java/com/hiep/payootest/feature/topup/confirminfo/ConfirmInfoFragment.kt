package com.hiep.payootest.feature.topup.confirminfo


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hiep.payootest.R
import com.hiep.payootest.RequestCode.SHOW_OTP_DIALOG
import com.hiep.payootest.feature.topup.TopUpBaseFragment
import com.hiep.payootest.feature.topup.paymentmethod.SelectPaymentMethodFragment
import com.hiep.payootest.model.TopUpParams
import kotlinx.android.synthetic.main.fragment_confirm_info.*
import java.text.DecimalFormat


class ConfirmInfoFragment : TopUpBaseFragment() {
    lateinit var viewModel: ConfirmInfoViewModel
    private var enableBack = true
    private var pressBack = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ConfirmInfoViewModel::class.java]
        viewModel.allowSendOTPLiveData.observe(this, Observer {
            topUpActivity?.setIsEnabledButtonNext(it)
        })
        viewModel.transfertLiveData.observe(this, Observer {
            topUpActivity?.hideNavigation()
            topUpActivity?.hideActionBarBack()
            enableBack = false
            tvTransferResult.text = it.data?.message ?: it.error?.message
        })
    }


    override fun onStart() {
        super.onStart()
        topUpActivity?.setSubTitle(R.string.title_confirm_info)
        if (enableBack)
            topUpActivity?.showActionBarBack()
        topUpActivity?.setTextButtonNext(R.string.pay)
        topUpParams = arguments?.let {
            TopUpParams.fromBundle(it)
        }
        viewModel.confirmInfo(topUpParams)
        updateUI()
    }

    private fun updateUI() {
        if (topUpActivity != null) {
            tvConfirmReceiverPhone.text = topUpParams?.receiverPhone ?: ""
            val formatter = DecimalFormat("#,###đ")
            val confirmAmount = formatter.format(topUpParams?.amount ?: 0)
            tvConfirmAmount.text = confirmAmount
            tvConfirmPaymentMethod.text =
                topUpParams?.paymentMethod?.display?.let { getString(it) } ?: ""
        }
    }

    private fun showOTPDialog() {
        val fragmentManager = topUpActivity?.supportFragmentManager
        if (fragmentManager != null) {
            val otpDialog = OTPDialogFragment()
            otpDialog.arguments = topUpParams?.toBundle()
            otpDialog.setTargetFragment(this, SHOW_OTP_DIALOG)
            otpDialog.show(fragmentManager, otpDialog.javaClass.name)
            viewModel.otpCount++
        }
    }

    override fun onBack() {
        if (enableBack)
            topUpActivity?.setFragment(SelectPaymentMethodFragment(), false)
        else {
            if (pressBack)
                topUpActivity?.finish()
            else {
                pressBack = true
                Toast.makeText(topUpActivity, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed({ pressBack = false }, 1000)
            }
        }
    }

    override fun onNext() {
        showOTPDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                SHOW_OTP_DIALOG -> {
                    val params = topUpParams
                    if (params != null) {
                        viewModel.transfer(params)
                    }
                }
            }
        }
    }
}
