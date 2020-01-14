package com.hiep.payootest.network

class ApiUtils {
    companion object {
        private const val BASE_URL = "https://5e16de5e22b5c600140cffdf.mockapi.io/"

        @JvmStatic
        fun getServiceApi(): ServiceApi {
            return RetrofitClient.getClient(BASE_URL).create(ServiceApi::class.java)
        }
    }
}