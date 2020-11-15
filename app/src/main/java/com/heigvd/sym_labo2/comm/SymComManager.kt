package com.heigvd.sym_labo2.comm

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors
import java.util.zip.Deflater
/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin, Simon Mattei, Bastien Potet
 * Date    : 13.11.2020
 */
class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {


    fun sendRequest(
        url: String,
        request: String,
        contentType: String,
        fastConnection: Boolean = true,
        compressed: Boolean = false
    ) {

        var myRequest = request
        // Start http thread (so it's not on the main)
        val httpHandler = HandlerThread("httpThread")
        httpHandler.start()
        // Start handler for the http request
        Handler(httpHandler.looper).postDelayed({
            val mURL = URL(url)

            with(mURL.openConnection() as HttpURLConnection) {
                // Settings
                setRequestProperty("Content-Type", contentType)
                doOutput = true // indicates POST method
                if (!fastConnection)
                    setRequestProperty("X-Network", "CSD")
                if (compressed) {
                    setRequestProperty("X-Content-Encoding", "deflate")
                    myRequest = compressContent(request)
                }

                Log.d(TAG, "sendRequest: Sending : " + myRequest)

                outputStream.write(myRequest.toByteArray())

                // Handle response
                val br: BufferedReader = if (responseCode in 100..399) {
                    BufferedReader(InputStreamReader(inputStream));
                } else {
                    BufferedReader(InputStreamReader(errorStream));
                }

                val responseBody = if (compressed) {
                    br.lines().collect(Collectors.joining())
                } else {
                    readCompressedResponse(br.lines().collect(Collectors.joining()))
                }

                Log.d(TAG, "sendRequest: Received : " + responseBody)

                communicationEventListener?.handleServerResponse(responseBody)
            }
        }, 0)
    }

    // No time... :(
    fun readCompressedResponse(response: String) : String {
        return ""
    }

    private fun compressContent(content: String) : String {
        // Encode a String into bytes
        val input = content.toByteArray(charset("UTF-8"))

        // Compress the bytes
        val output = ByteArray(100)
        val compresser = Deflater()
        compresser.setInput(input)
        compresser.finish()

        val compressedDataLength = compresser.deflate(output)
        compresser.end()

        return output.toString()
    }

    companion object {
        private const val TAG: String = "SymComManager"
    }
}