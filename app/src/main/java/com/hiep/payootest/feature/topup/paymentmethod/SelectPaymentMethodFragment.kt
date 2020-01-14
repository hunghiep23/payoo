package com.hiep.payootest.feature.topup.paymentmethod


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.annotation.IdRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hiep.payootest.feature.topup.TopUpBaseFragment
import com.hiep.payootest.PaymentMethod
import com.hiep.payootest.R
import com.hiep.payootest.feature.topup.confirminfo.ConfirmInfoFragment
import com.hiep.payootest.feature.topup.enterphonenumber.EnterPhoneFragment
import com.hiep.payootest.model.TopUpParams
import kotlinx.android.synthetic.main.fragment_select_payment_method.*

class SelectPaymentMethodFragment : TopUpBaseFragment() {
    lateinit var viewModel: SelectPaymentMethodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        topUpActivity?.setSubTitle(R.string.title_select_payment_method)
        topUpActivity?.showActionBarBack()
        topUpActivity?.setTextButtonNext(R.string.next)
        topUpParams = arguments?.let {
            TopUpParams.fromBundle(it)
        }
        viewModel.applyRules(topUpParams)
        topUpActivity?.setIsEnabledButtonNext(viewModel.allowNextStepLiveData.value!!)
    }

    private fun initView() {
        rbPayoo.setOnCheckedChangeListener(onPaymentMethodChangeCheckListener)
        rbLinkedAccount.setOnCheckedChangeListener(onPaymentMethodChangeCheckListener)
        rbInternationalCard.setOnCheckedChangeListener(onPaymentMethodChangeCheckListener)
        rbDomesticCard.setOnCheckedChangeListener(onPaymentMethodChangeCheckListener)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[SelectPaymentMethodViewModel::class.java]
        viewModel.allowNextStepLiveData.observe(this, Observer {
            topUpActivity?.setIsEnabledButtonNext(it)
        })
        viewModel.isEnabledMethodLiveData.observe(this, Observer { enableMap ->
            enableMap.forEach {
                val (method, enable) = it
                val rb = findView(method.id)
                if (!enable && (rb as? RadioButton)?.isChecked == true) {
                    rgPaymentMethod.clearCheck()
                    viewModel.paymentMethod = null
                }
                rb?.isEnabled = enable
            }
        })
    }

    private val onPaymentMethodChangeCheckListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView != null && isChecked)
                viewModel.paymentMethod = PaymentMethod.of(buttonView.id)
        }

    private fun findView(@IdRes id: Int): View? {
        return when (id) {
            R.id.rbLinkedAccount -> rbLinkedAccount
            R.id.rbPayoo -> rbPayoo
            R.id.rbDomesticCard -> rbDomesticCard
            R.id.rbInternationalCard -> rbInternationalCard
            else -> null
        }
    }

    override fun onBack() {
        topUpActivity?.setFragment(EnterPhoneFragment(), false)
    }

    override fun onNext() {
        val nextFragment = ConfirmInfoFragment()
        topUpParams = topUpParams?.newPaymentMethod(viewModel.paymentMethod)
        nextFragment.arguments = topUpParams?.toBundle()
        topUpActivity?.setFragment(nextFragment, true)
    }

}
