package com.hiep.payootest.feature.topup

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hiep.payootest.R
import com.hiep.payootest.model.TopUpParams

abstract class TopUpBaseFragment : Fragment() {
    val topUpActivity: TopUpActivity?
        get() = activity as? TopUpActivity
    var topUpParams: TopUpParams? = null

    protected fun isGrantedPermission(permission: String): Boolean {
        return (activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                permission
            )
        } == PackageManager.PERMISSION_GRANTED)
    }

    protected fun requestPermission(permissionArray: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            activity?.apply {
                ActivityCompat.requestPermissions(
                    this,
                    permissionArray,
                    requestCode
                )
            }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onPause()
            onStop()
        } else {
            onStart()
            onResume()
        }
    }

    abstract fun onBack()
    abstract fun onNext()
}