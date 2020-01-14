package com.hiep.payootest.model

import android.os.Bundle
import com.hiep.payootest.PaymentMethod

class TopUpParams(
    val senderPhone: String?,
    val receiverPhone: String?,
    val amount: Long?,
    val paymentMethod: PaymentMethod?
) {
    constructor(builder: Builder) : this(
        builder.senderPhone,
        builder.receiverPhone,
        builder.amount,
        builder.paymentMethod
    )

    constructor(senderPhone: String?, receiverPhone: String?, amount: Long?) : this(
        senderPhone,
        receiverPhone,
        amount,
        null
    )

    fun toBundle(): Bundle = Bundle().apply {
        putString(TOP_UP_PHONE_SENDER, senderPhone)
        putString(TOP_UP_PHONE_RECEIVER, receiverPhone)
        putLong(TOP_UP_AMOUNT, amount ?: 0)
        putInt(TOP_UP_PAYMENT_METHOD_ID, paymentMethod?.id ?: 0)
    }

    fun newPaymentMethod(paymentMethod: PaymentMethod?) =
        Builder()
            .senderPhone(senderPhone)
            .receiverPhone(receiverPhone)
            .amount(amount)
            .paymentMethod(paymentMethod)
            .build()

    class Builder {
        var senderPhone: String? = null
        var receiverPhone: String? = null
        var amount: Long? = null
        var paymentMethod: PaymentMethod? = null

        fun senderPhone(senderPhone: String?) = apply { this.senderPhone = senderPhone }

        fun receiverPhone(receiverPhone: String?) = apply { this.receiverPhone = receiverPhone }

        fun amount(amount: Long?): Builder = apply { this.amount = amount }

        fun paymentMethod(paymentMethod: PaymentMethod?) =
            apply { this.paymentMethod = paymentMethod }

        fun build() = TopUpParams(this)
    }

    companion object {
        const val TOP_UP_PHONE_SENDER = "top_up_phone_sender"
        const val TOP_UP_PHONE_RECEIVER = "top_up_phone_receiver"
        const val TOP_UP_AMOUNT = "top_up_amount"
        const val TOP_UP_PAYMENT_METHOD_ID = "top_up_payment_method_id"

        @JvmStatic
        fun fromBundle(bundle: Bundle): TopUpParams = TopUpParams(
            bundle.getString(TOP_UP_PHONE_SENDER),
            bundle.getString(TOP_UP_PHONE_RECEIVER),
            bundle.getLong(TOP_UP_AMOUNT),
            PaymentMethod.of(bundle.getInt(TOP_UP_PAYMENT_METHOD_ID))
        )
    }
}