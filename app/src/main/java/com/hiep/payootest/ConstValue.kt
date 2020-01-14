package com.hiep.payootest

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import java.io.Serializable

object RequestCode {
    const val PERMISSIONS = 1
    const val READ_CONTACTS = 2
    const val SHOW_OTP_DIALOG = 3
}

enum class PaymentMethod(@IdRes val id: Int, @StringRes val display: Int){
    Payoo(R.id.rbPayoo, R.string.payment_method_payoo) {
        override fun toString() = "payoo"
    },
    InternationalCard(R.id.rbInternationalCard, R.string.payment_method_international_card) {
        override fun toString() = "international_card"
    },
    DomesticCard(R.id.rbDomesticCard, R.string.payment_method_domestic_card) {
        override fun toString() = "domestic_card"
    },
    LinkedAccount(R.id.rbLinkedAccount, R.string.payment_method_linked_account) {
        override fun toString() = "linked_account"
    };

    companion object {
        @JvmStatic
        fun of(id: Int): PaymentMethod? = values().firstOrNull { it.id == id }

    }
}