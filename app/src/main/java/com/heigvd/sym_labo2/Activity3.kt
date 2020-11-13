package com.heigvd.sym_labo2

import android.os.Bundle
import android.sax.Element
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import org.xmlpull.v1.XmlSerializer


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
                    if(isJson) {
                        val obj = deserializeToMyObjectFromJson(response)
                        receivedTextView.text = "Received JSON :\nobject(" + obj.dataString + ", " + obj.dataInt + ")"
                    } else if (isXml) {
                        val obj = deserializeToMyObjectFromXML(response)
                        receivedTextView.text = "Received XML\n object(" + obj.dataString + ", " + obj.dataInt + ")"
                    }
                    canSendAgain = true
                }
            }
        })

        sendButton.setOnClickListener {

            val sendObject = MyObject(
                inputTextToSend.text.toString(), Integer.parseInt(
                    inputIntToSend.text.toString()
                )
            )

            if(byJson.isChecked) {
                isJson = true
                canSendAgain = false
                mcm.sendRequest(
                    MainActivity.LAB_SERVER + "rest/json", serializeToJsonString(
                        sendObject
                    ), "application/json"
                )
            } else if(byXML.isChecked) {
                isXml = true
                canSendAgain = false
                mcm.sendRequest(
                    MainActivity.LAB_SERVER + "rest/xml", serializeToXmlString(
                        sendObject
                    ), "application/xml"
                )
            }
        }
    }

    fun serializeToJsonString(myObject: MyObject) : String {
        return Json.encodeToString(MyObject.serializer(), myObject)
    }

    fun deserializeToMyObjectFromJson(jsonContent: String) : MyObject {
        // ignoring unknown keys such as "infos":"..."
        return Json{ ignoreUnknownKeys = true }.decodeFromString(MyObject.serializer(), jsonContent)
    }

    fun serializeToXmlString(myObject: MyObject) : String {
        // todo
        return ""
    }

    fun deserializeToMyObjectFromXML(jsonContent: String) : MyObject {
        return Json{ ignoreUnknownKeys = true }.decodeFromString(MyObject.serializer(), jsonContent)
    }

    companion object {
        private var canSendAgain: Boolean = true
        private var isJson: Boolean = false
        private var isXml: Boolean = false
    }
}