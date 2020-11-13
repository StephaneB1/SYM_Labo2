package com.heigvd.sym_labo2

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {


    fun sendRequest(url: String, request: String, contentType: String) {
        // Start http thread (so it's not on the main)
        var httpHandler = HandlerThread("httpThread")
        httpHandler.start()
        // Start handler for the http request
        Handler(httpHandler.looper).postDelayed({
            val mURL = URL(url)

            with(mURL.openConnection() as HttpURLConnection) {
                setRequestProperty("Content-Type", contentType)
                doOutput = true // indicates POST method
                //doInput= true
                Log.d(TAG, "sendRequest: Sending : " + request)
                outputStream.write(request.toByteArray())

                // Handle response
                val br: BufferedReader
                if (responseCode in 100..399) {
                    br = BufferedReader(InputStreamReader(inputStream));
                } else {
                    br = BufferedReader(InputStreamReader(errorStream));
                }

                var result = ""
                while (br.readLine() != null) {
                    result += br.readLine() + "\n"
                }

                Log.d(TAG, "sendRequest: Received : " + result)

                communicationEventListener?.handleServerResponse(result)
            }
        }, 0)
    }

    companion object {
        private const val TAG: String = "SymComManager"
    }
}