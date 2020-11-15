package com.heigvd.sym_labo2.comm

interface CommunicationEventListener {
    fun handleServerResponse(response :String)
}