package com.heigvd.sym_labo2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.work.*
import java.net.HttpURLConnection
import java.net.URL

class Activity2 : AppCompatActivity() {

    private lateinit var inputToSend: EditText
    private lateinit var receivedTextView: TextView
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        inputToSend = findViewById(R.id.input_text)
        receivedTextView = findViewById(R.id.received_txt)
        sendButton = findViewById(R.id.send_button)

        sendButton.setOnClickListener {
            // Send the request via WorkManager (if offline it will retry)

            var httpHandler = HandlerThread("httpThread")
            httpHandler.start()
            // Start handler for the http request
            Handler(httpHandler.looper).postDelayed({

                // Http worker
                val compressionWork = OneTimeWorkRequest.Builder(HttpWorker::class.java)

                // Set the worker's parameters
                val data = Data.Builder()
                data.putString("target_url", MainActivity.LAB_SERVER + "rest/txt")
                data.putString("target_request", "help")
                compressionWork.setInputData(data.build())

                // Enqueue worker
                WorkManager.getInstance().enqueue(compressionWork.build())
            }, 0)
        }

    }

    class HttpWorker(appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val mURL = URL(inputData.getString("target_url"))
            val request = inputData.getString("target_request")

            with(mURL.openConnection() as HttpURLConnection) {
                setRequestProperty("Content-Type", "txt/plain")
                doOutput = true // indicates POST method

                outputStream.write(request?.toByteArray())
                if (responseCode != 200) {
                    Log.d(TAG, "doWork: error, retrying...")
                    return Result.retry()
                } else {
                    return Result.success()
                }
            }
        }
    }

    companion object {
        private const val TAG: String = "Activity2"
    }
}