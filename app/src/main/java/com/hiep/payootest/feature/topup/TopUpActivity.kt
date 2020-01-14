package com.hiep.payootest.feature.topup

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hiep.payootest.PaymentMethod
import com.hiep.payootest.R
import com.hiep.payootest.feature.topup.enterphonenumber.EnterPhoneFragment
import com.hiep.payootest.model.FaceValue
import com.hiep.payootest.model.State
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.layout_actionbar_topup.*


class TopUpActivity : AppCompatActivity() {
    var currentFragment: TopUpBaseFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)
        initView()

        initFirstState()
    }

    private fun initView() {
        setSupportActionBar(toolbarTopUp)
        actionbarBack.setOnClickListener(clickListener)
        btnNext.setOnClickListener(clickListener)
    }

    private fun initFirstState() {
        setFragment(EnterPhoneFragment(), true)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.actionbarBack -> {
                onBackPressed()
            }
            R.id.btnNext -> {
                currentFragment?.onNext()
            }
        }
    }

    override fun onBackPressed() {
        currentFragment?.onBack()
    }

    fun setFragment(fragment: TopUpBaseFragment, next: Boolean) {
        val tag = fragment.javaClass.name
        val manager = supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        if (currentFragment != null) {
            if (next)
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_right_to_center,
                    R.anim.slide_center_to_left
                )
            else
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_left_to_center,
                    R.anim.slide_center_to_right
                )
        }
        if (currentFragment?.isAdded == true) {
            fragmentTransaction.hide(currentFragment as Fragment)
        }
        val existFragment = manager.findFragmentByTag(tag)
        existFragment?.apply {
            this.arguments = fragment.arguments ?: this.arguments //update new params
            currentFragment = this as? TopUpBaseFragment
            fragmentTransaction.show(this)
        } ?: let {
            currentFragment = fragment
            fragmentTransaction.add(R.id.flTopUpContainer, fragment, tag)
        }
        fragmentTransaction.commit()
    }

    fun setSubTitle(@StringRes stringId: Int) {
        tvSubTitle.text = getString(stringId)
    }

    fun setIsEnabledButtonNext(enabled: Boolean) {
        btnNext.isEnabled = enabled
    }


    fun setAmount(faceValue: FaceValue?) {
        val amountText =
            if (faceValue != null) getString(
                R.string.transfer_amount,
                faceValue.formattedValue
            ) else ""
        tvAmount.text = amountText
    }

    fun showActionBarBack() {
        actionbarBack.visibility = View.VISIBLE
    }

    fun hideActionBarBack() {
        actionbarBack.visibility = View.GONE
    }

    fun setTextButtonNext(@StringRes stringId: Int) {
        btnNext.text = getString(stringId)
    }

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideNavigation(){
        rlNavigation.visibility=View.INVISIBLE
    }
}
