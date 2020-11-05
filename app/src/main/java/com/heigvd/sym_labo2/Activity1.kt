package com.heigvd.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Activity1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        var mcm = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                Log.d(TAG, "handleServerResponse: " + response)
            }
        })
        mcm.sendRequest(MainActivity.LAB_SERVER, "txt")
    }

    companion object {
        private const val TAG: String = "Activity1"
    }
}