package com.heigvd.sym_labo2

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {


    fun sendRequest(url: String, request: String) {
        var httpHandler = HandlerThread("httpThread")
        httpHandler.start()
        Handler(httpHandler.looper).postDelayed( {
            val mURL = URL("$url?text=$request")

            with(mURL.openConnection() as HttpURLConnection) {
                setRequestProperty("Content-Type", "txt/plain")
                requestMethod = "POST"
                communicationEventListener?.handleServerResponse(responseMessage)
            }
        }, 0)
    }

    companion object {
        private const val TAG: String = "SymComManager"
    }
}