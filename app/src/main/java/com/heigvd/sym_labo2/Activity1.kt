package com.heigvd.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class Activity1 : AppCompatActivity() {


    private lateinit var sendingText: TextView
    private lateinit var receivedTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        sendingText = findViewById(R.id.sending_text)
        receivedTextView = findViewById(R.id.received_txt)

        val mcm = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                receivedTextView.text = "Received from server : $response"
            }
        })

        val text = "well well well"
        mcm.sendRequest(MainActivity.LAB_SERVER + "rest/txt", text)
        sendingText.text = "Sending to server : $text"
    }

    companion object {
        private const val TAG: String = "Activity1"
    }
}