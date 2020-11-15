package com.heigvd.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.heigvd.sym_labo2.comm.CommunicationEventListener
import com.heigvd.sym_labo2.comm.SymComManager
/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin, Simon Mattei, Bastien Potet
 * Date    : 13.11.2020
 */
class Activity4 : AppCompatActivity() {

    private lateinit var sendingText: TextView
    private lateinit var receivedTime4GView: TextView
    private lateinit var receivedTime2GView: TextView
    private lateinit var receivedTimeComp4GView: TextView
    private lateinit var receivedTimeComp2GView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)


        sendingText = findViewById(R.id.sending_text)
        receivedTime4GView = findViewById(R.id.received_time_4g)
        receivedTime2GView = findViewById(R.id.received_time_2g)
        receivedTimeComp4GView = findViewById(R.id.received_time_comp_4g)
        receivedTimeComp2GView = findViewById(R.id.received_time_comp_2g)

        // Timers
        var start4g = 0L
        var end4g = 0L
        var start2g = 0L
        var end2g = 0L
        var startComp4g = 0L
        var endComp4g = 0L
        var startComp2g = 0L
        var endComp2g = 0L

        val mcm4G = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                end4g = System.currentTimeMillis()
                runOnUiThread {
                    receivedTime4GView.text = "Time with 4G : ${end4g - start4g}ms"
                }
            }
        })

        val mcm2G = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                end2g = System.currentTimeMillis()
                runOnUiThread {
                    receivedTime2GView.text = "Time with 2G : ${end2g - start2g}ms"
                }
            }
        })

        val mcmCompressed4G = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                endComp4g = System.currentTimeMillis()
                runOnUiThread {
                    receivedTimeComp4GView.text = "Time with 4G (compressed) : ${endComp4g - startComp4g}ms"
                }
            }
        })

        val mcmCompressed2G = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                endComp2g = System.currentTimeMillis()
                runOnUiThread {
                    receivedTimeComp2GView.text = "Time with 2G (compressed) : ${endComp2g - startComp2g}ms"
                }
            }
        })

        // Random string of 2000 characters for compression tests
        val charPool = "abcdefghijklmnopqrstuvwxyz"
        val randomString = (1..2000)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.length) }
            .map(charPool::get)
            .joinToString("")

        sendingText.text = "Sending " + randomString.length + " characters"

        // Send request with 4g connection
        start4g = System.currentTimeMillis()
        mcm4G.sendRequest(MainActivity.LAB_SERVER + "rest/txt",  randomString, "txt/plain")

        // Send request with 2g connection
        start2g = System.currentTimeMillis()
        mcm2G.sendRequest(MainActivity.LAB_SERVER + "rest/txt",  randomString, "txt/plain", false)

        // Send request with 4g connection with compressed content
        startComp4g = System.currentTimeMillis()
        mcmCompressed4G.sendRequest(MainActivity.LAB_SERVER + "rest/txt",  randomString, "txt/plain")

        // Send request with 2g connection with compressed content
        startComp2g = System.currentTimeMillis()
        mcmCompressed2G.sendRequest(MainActivity.LAB_SERVER + "rest/txt",  randomString, "txt/plain", false)
    }
}