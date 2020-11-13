package com.heigvd.sym_labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class Activity3 : AppCompatActivity() {

    private lateinit var inputTextToSend: EditText
    private lateinit var inputIntToSend: EditText

    private lateinit var receivedTextView: TextView
    private lateinit var sendButton: Button
    private lateinit var byJson: RadioButton
    private lateinit var byXML: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        inputTextToSend = findViewById(R.id.input_text)
        inputIntToSend = findViewById(R.id.input_int)
        receivedTextView = findViewById(R.id.received_txt)
        byJson = findViewById(R.id.send_by_json)
        byXML = findViewById(R.id.send_by_xml)
        sendButton = findViewById(R.id.send_button)


        val mcm = SymComManager(object : CommunicationEventListener {
            override fun handleServerResponse(response: String) {
                runOnUiThread {
                    receivedTextView.text = "Received : $response"
                }
            }
        })

        sendButton.setOnClickListener {

            var sendObject = MyObject(inputTextToSend.text.toString(), Integer.parseInt(inputIntToSend.text.toString()))

            if(byJson.isChecked) {
                mcm.sendRequest(MainActivity.LAB_SERVER + "rest/json", serializeToJsonString(sendObject), "application/json")
            } else if(byXML.isChecked) {

            }

        }
    }

    fun serializeToJsonString(myObject: MyObject) : String {
        return Json.encodeToString(MyObject.serializer(), myObject)
    }

    fun deserializeToMyObject(jsonContent: JsonElement) : MyObject {
        return Json.decodeFromJsonElement(MyObject.serializer(), jsonContent)
    }
}