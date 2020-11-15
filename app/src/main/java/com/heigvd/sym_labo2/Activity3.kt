package com.heigvd.sym_labo2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.heigvd.sym_labo2.comm.CommunicationEventListener
import com.heigvd.sym_labo2.comm.SymComManager
import com.heigvd.sym_labo2.models.*
import kotlinx.serialization.json.Json
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList


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
                    if (isJson) {
                        val obj = deserializeToMyObjectFromJson(response)
                        receivedTextView.text =
                            "Received JSON :\nobject(" + obj.dataString + ", " + obj.dataInt + ")"
                    } else if (isXml) {
                        val obj = deserializeToMyObjectFromXML(response)
                        var result = "Received XML :\n"
                        for(person in obj) {
                            result += "- " + person.name + "[" + person.gender  + "] :\n"
                            for(num in person.numbers)
                                result += "\t" + num.type + " : " + num.number + "\n"
                        }

                        receivedTextView.text = result
                    }
                    canSendAgain = true
                }
            }
        })

        sendButton.setOnClickListener {

            if(byJson.isChecked) {
                val sendObject = MyObject(
                    inputTextToSend.text.toString(), Integer.parseInt(
                        inputIntToSend.text.toString()
                    )
                )

                isJson = true
                canSendAgain = false
                mcm.sendRequest(
                    MainActivity.LAB_SERVER + "rest/json", serializeToJsonString(
                        sendObject
                    ), "application/json"
                )
            } else if(byXML.isChecked) {

                val directory = ArrayList<Person>()

                directory.add(
                    Person("Doe", "John","", GENDER.MAN,
                    ArrayList(listOf(
                    PhoneNumber(NUMBER_TYPE.HOME, "0219329021"),
                    PhoneNumber(NUMBER_TYPE.MOBILE, "0798576472")
                )))
                )
                directory.add(
                    Person("Hemming", "Sarah","Jessica", GENDER.WOMAN,
                    ArrayList(listOf(
                    PhoneNumber(NUMBER_TYPE.HOME, "0219483040"),
                    PhoneNumber(NUMBER_TYPE.WORK, "0885904300")
                )))
                )
                directory.add(
                    Person("testUser", inputTextToSend.text.toString(),"", GENDER.OTHER,
                    ArrayList(listOf(
                    PhoneNumber(NUMBER_TYPE.HOME, inputIntToSend.text.toString())
                )
                    ))
                )

                isXml = true
                canSendAgain = false
                mcm.sendRequest(
                    MainActivity.LAB_SERVER + "rest/xml", serializeToXmlString(
                        directory
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

    fun serializeToXmlString(directory: ArrayList<Person>) : String {

        var xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE directory SYSTEM \"http://sym.iict.ch/directory.dtd\">" +
                "<directory>"

        for(person in directory) {
            xmlString += "<person>"
            xmlString +=    "<name>" + person.name + "</name>"
            xmlString +=    "<firstname>" + person.firstName + "</firstname>"
            if(person.middleName != "")
                xmlString +=    "<middlename>" + person.firstName + "</middlename>"
            xmlString +=    "<gender>" + person.gender + "</gender>"
            for(phoneNumber in person.numbers)
                xmlString +=    "<phone type=\"" + phoneNumber.type.toString().toLowerCase(Locale.ROOT) + "\">" + phoneNumber.number + "</phone>"
            xmlString += "</person>"
        }

        xmlString += "</directory>"

        return xmlString
    }

    fun deserializeToMyObjectFromXML(xmlContent: String) : ArrayList<Person> {
        val directory = ArrayList<Person>()
        val builder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val src = InputSource()
        src.characterStream = StringReader(xmlContent)
        val doc: Document = builder.parse(src)

        if(doc.getElementsByTagName("directory") != null) {
            val dirElement = doc.getElementsByTagName("directory").item(0)

            for(index in 0..dirElement.childNodes.length) {
                if(dirElement.childNodes.item(index) != null &&
                    dirElement.childNodes.item(index).nodeName == "person") {
                    val attr = dirElement.childNodes.item(index).childNodes
                    var name = ""
                    var firstname = ""
                    var middlename = ""
                    var gender = GENDER.OTHER
                    val numbers = ArrayList<PhoneNumber>()

                    for(attrIndex in 0..attr.length) {
                        if(attr.item(attrIndex) != null) {
                            when (attr.item(attrIndex).nodeName) {
                                "name" -> name = attr.item(attrIndex).textContent
                                "firstname" -> firstname = attr.item(attrIndex).textContent
                                "middlename" -> middlename = attr.item(attrIndex).textContent
                                "gender" -> gender = GENDER.valueOf(attr.item(attrIndex).textContent)
                                "phone" -> {
                                    numbers.add(
                                        PhoneNumber(
                                            NUMBER_TYPE.valueOf(
                                                attr.item(attrIndex).attributes.item(0).textContent
                                                    .toUpperCase(Locale.ROOT)
                                            ),
                                            attr.item(attrIndex).textContent
                                        )
                                    )
                                }
                            }
                        }
                    }

                    directory.add(Person(name, firstname, middlename, gender, numbers))
                }
            }
        }

        return directory
    }

    companion object {
        private const val TAG: String = "Activity3"
        private var canSendAgain: Boolean = true
        private var isJson: Boolean = false
        private var isXml: Boolean = false
    }
}