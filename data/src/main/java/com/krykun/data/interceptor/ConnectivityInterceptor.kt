package com.krykun.data.interceptor

import android.content.Context
import com.krykun.data.ext.hasNetwork
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (context.hasNetwork() == false) {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message("No internet connection")
                .body(ResponseBody.create(null, "{${IOException("No internet connection")}}"))
                .build()
        } else {
            chain.proceed(chain.request())
        }
    }
}