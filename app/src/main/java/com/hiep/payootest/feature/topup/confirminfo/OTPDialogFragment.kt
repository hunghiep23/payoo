package com.hiep.payootest.feature.topup.confirminfo

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.hiep.payootest.R
import com.hiep.payootest.model.TopUpParams
import kotlinx.android.synthetic.main.layout_otp.*

class OTPDialogFragment : DialogFragment() {
    var topUpParams: TopUpParams? = null
    lateinit var viewModel: OTPViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_otp, container);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        btnSendOTP.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener {
        when (it?.id) {
            R.id.btnSendOTP -> viewModel.sendOTP(edtOTP.text.toString())
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[OTPViewModel::class.java]
        viewModel.otpLiveData.observe(this, Observer {
            if (it) {
                finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { topUpParams = TopUpParams.fromBundle(it) }
        tvOTPInform.text = getString(R.string.require_input_otp, topUpParams?.senderPhone)
    }

    private fun finish() {
        targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, Intent());
        dismiss()
    }
}