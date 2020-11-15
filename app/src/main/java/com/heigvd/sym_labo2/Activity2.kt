package com.heigvd.sym_labo2

import android.content.Context
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
import java.util.concurrent.TimeUnit
/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin, Simon Mattei, Bastien Potet
 * Date    : 13.11.2020
 */
class Activity2 : AppCompatActivity() {

    private lateinit var inputToSend: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        inputToSend = findViewById(R.id.input_text)
        receivedTextView = findViewById(R.id.received_txt)
        sendButton = findViewById(R.id.send_button)

        sendButton.setOnClickListener {
            // Send the request via WorkManager (if offline it will retry)

            val httpHandler = HandlerThread("httpThread2")
            httpHandler.start()
            // Start handler for the http request
            Handler(httpHandler.looper).postDelayed({

                // Http worker builder with Backoff criteria (retry policy)
                val compressionWorkBuilder = OneTimeWorkRequestBuilder<HttpWorker>()
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS)

                // Set the worker's parameters
                val data = Data.Builder()
                val url =  MainActivity.LAB_SERVER + "rest/txt"
                data.putString("target_url", url)
                data.putString("target_request", inputToSend.text.toString())
                compressionWorkBuilder.setInputData(data.build())

                // Enqueue worker
                WorkManager.getInstance(this).enqueue(compressionWorkBuilder.build())
            }, 0)
        }

    }

    class HttpWorker(appContext: Context, workerParams: WorkerParameters):
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            val mURL = URL(inputData.getString("target_url"))
            val request = inputData.getString("target_request")

            try {
                with(mURL.openConnection() as HttpURLConnection) {
                    setRequestProperty("Content-Type", "txt/plain")
                    doOutput = true // indicates POST method

                    outputStream.write(request?.toByteArray())
                    return if (responseCode != 200) {
                        receivedTextView.text = "Sorry couldn't reach the server, trying again in a bit..."
                        Result.retry()
                    } else {
                        receivedTextView.text = "Alright all good!"
                        Result.success()
                    }
                }
            } catch (e: Exception) {
                return Result.retry()
            }

        }
    }

    companion object {
        private const val TAG: String = "Activity2"
        private lateinit var receivedTextView: TextView
    }
}