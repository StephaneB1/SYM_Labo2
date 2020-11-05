package com.heigvd.sym_labo2

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {


    fun sendRequest(url: String, request: String) {
        // Start http thread (so it's not on the main)
        var httpHandler = HandlerThread("httpThread")
        httpHandler.start()
        // Start handler for the http request
        Handler(httpHandler.looper).postDelayed( {
            val mURL = URL(url)

            with(mURL.openConnection() as HttpURLConnection) {
                setRequestProperty("Content-Type", "txt/plain")
                doOutput = true // indicates POST method
                //doInput= true
                outputStream.write(request.toByteArray())
                communicationEventListener?.handleServerResponse(responseMessage)
            }
        }, 0)
    }

    companion object {
        private const val TAG: String = "SymComManager"
    }
}