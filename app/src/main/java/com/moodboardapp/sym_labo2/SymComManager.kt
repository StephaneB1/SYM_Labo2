package ch.heigvd.iict.sym.lab.comm

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

    fun sendRequest(url: String, request: String) {
        Handler(Looper.getMainLooper()).postDelayed( {
            val mURL = URL(url)

            with(mURL.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                //val wr = OutputStreamWriter(getOutputStream());
                //wr.write(request);
                //wr.flush();

                communicationEventListener?.handleServerResponse(responseMessage)
            }
        }, 0)
    }

    companion object {
        private const val TAG: String = "SymComManager"
    }
}