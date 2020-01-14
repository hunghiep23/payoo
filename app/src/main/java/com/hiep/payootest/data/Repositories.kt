package com.hiep.payootest.data

import com.hiep.payootest.model.json.TransferBody
import com.hiep.payootest.model.json.TransferResponseJson
import com.hiep.payootest.network.ApiUtils
import io.reactivex.Single


class Repositories {
    fun transfer(transferBody: TransferBody):Single<TransferResponseJson>{
        return ApiUtils.getServiceApi().transfer(transferBody)
    }
}