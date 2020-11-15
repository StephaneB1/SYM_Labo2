package com.heigvd.sym_labo2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.heigvd.sym_labo2.comm.CommunicationEventListener
import com.heigvd.sym_labo2.comm.SymComManager

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
                runOnUiThread {
                    receivedTextView.text = "Received : $response"
                }
            }
        })

        val text = "well well well"
        mcm.sendRequest(MainActivity.LAB_SERVER + "rest/txt", text, "txt/plain")
        sendingText.text = "Sending : $text"
    }

    companion object {
        private const val TAG: String = "Activity1"
    }
}