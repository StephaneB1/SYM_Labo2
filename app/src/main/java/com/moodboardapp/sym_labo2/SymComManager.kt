package com.moodboardapp.sym_labo2

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

    fun sendRequest(url: String, request: String) {
        /*
        val urlConnection = URL(url + request).openConnection() as HttpURLConnection
        try {
            readStream(BufferedInputStream(urlConnection.inputStream))
        } finally {
            urlConnection.disconnect()
        }*/
    }

    private fun readStream(inputStream: InputStream) {

        val response: String
        val reader = BufferedReader(inputStream.reader())
        try {
            response = reader.readText()
        } finally {
            reader.close()
        }
        communicationEventListener?.handleServerResponse(response)
    }


}