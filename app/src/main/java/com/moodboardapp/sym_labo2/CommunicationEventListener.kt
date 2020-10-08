package ch.heigvd.iict.sym.lab.comm

interface CommunicationEventListener {
    fun handleServerResponse(response :String)
}