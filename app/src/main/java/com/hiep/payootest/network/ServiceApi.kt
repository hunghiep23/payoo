package com.hiep.payootest.network

import com.hiep.payootest.model.json.TransferBody
import com.hiep.payootest.model.json.TransferResponseJson
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceApi {
    @POST("/api/v1/transfer")
    fun transfer(@Body transferBody: TransferBody):Single<TransferResponseJson>
}