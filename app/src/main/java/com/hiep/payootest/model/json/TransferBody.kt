package com.hiep.payootest.model.json

data class TransferBody(
    val sender_phone: String?,
    val receiver_phone: String?,
    val amount: Long?,
    val payment_method: String?
) {
    constructor() : this(null, null, null, null)
}