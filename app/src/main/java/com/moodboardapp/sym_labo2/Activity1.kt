package com.moodboardapp.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Activity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        var scm = SymComManager()
        scm.communicationEventListener = object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                println(response)
            }
        }
        scm.sendRequest(getString(R.string.url), "rest/txt")
    }
}