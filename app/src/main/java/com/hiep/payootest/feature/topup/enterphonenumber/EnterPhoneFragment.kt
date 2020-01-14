package com.hiep.payootest.feature.topup.enterphonenumber


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.hiep.payootest.feature.topup.TopUpBaseFragment
import com.hiep.payootest.R
import com.hiep.payootest.RequestCode
import com.hiep.payootest.feature.contact.ListContactActivity
import com.hiep.payootest.feature.topup.paymentmethod.SelectPaymentMethodFragment
import com.hiep.payootest.model.FaceValue
import com.hiep.payootest.model.TopUpParams
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneFragment : TopUpBaseFragment() {
    lateinit var viewModel: EnterPhoneViewModel
    lateinit var faceValueAdapter: ListFaceValueAdapter
    var pressBack = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()


    }

    private fun initView() {
        imgBtnOpenContact.setOnClickListener(onClickListener)
        edtPhone.addTextChangedListener(onPhoneChangeTextListener)
        faceValueAdapter = ListFaceValueAdapter { faceValue: FaceValue, position: Int ->
            faceValueAdapter.updateFaceValueState(position, true)
            viewModel.amount = faceValue.value
            topUpActivity?.setAmount(faceValue)
        }
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rvListAmount.layoutManager = gridLayoutManager
        rvListAmount.adapter = faceValueAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[EnterPhoneViewModel::class.java]
        viewModel.allowNextStepLiveData.observe(this, Observer { allow ->
            topUpActivity?.setIsEnabledButtonNext(allow)

        })
        viewModel.amountListLiveData.observe(this, Observer {
            faceValueAdapter.setList(it)
        })
    }


    override fun onStart() {
        super.onStart()
        topUpActivity?.setSubTitle(R.string.title_receiver_info)
        topUpActivity?.hideActionBarBack()
        topUpActivity?.setTextButtonNext(R.string.next)
        topUpActivity?.setIsEnabledButtonNext(viewModel.allowNextStepLiveData.value!!)
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.imgBtnOpenContact -> {
                if (isGrantedPermission(Manifest.permission.READ_CONTACTS)) {
                    gotoListContactActivity()
                } else requestPermission(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    RequestCode.PERMISSIONS
                )
            }
        }
    }
    private val onPhoneChangeTextListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.receiverPhone = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED && permission == Manifest.permission.READ_CONTACTS)
                gotoListContactActivity()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.READ_CONTACTS -> {
                    edtPhone.setText(data?.data?.toString())
                }
            }
        }
    }

    private fun gotoListContactActivity() {
        topUpActivity?.let {
            startActivityForResult(
                ListContactActivity.newInstance(it),
                RequestCode.READ_CONTACTS
            )
        }
    }

    override fun onBack() {
        if (pressBack)
            topUpActivity?.finish()
        else {
            pressBack = true
            Toast.makeText(topUpActivity, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT)
                .show()
            Handler().postDelayed({ pressBack = false }, 1000)
        }
    }

    override fun onNext() {
        topUpActivity?.hideKeyboard()
        val nextFragment = SelectPaymentMethodFragment()
        topUpParams = TopUpParams(viewModel.senderPhone, viewModel.receiverPhone, viewModel.amount)
        nextFragment.arguments = topUpParams?.toBundle()
        topUpActivity?.setFragment(nextFragment, true)
    }
}
