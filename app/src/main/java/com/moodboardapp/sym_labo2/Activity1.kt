package com.moodboardapp.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.concurrent.thread

class Activity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val scm = SymComManager()
        scm.communicationEventListener = object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                findViewById<TextView>(R.id.received_content).text = response
            }
        }
        scm.sendRequest(getString(R.string.url) + "rest/txt", "txt/plain")
    }
}