package com.heigvd.sym_labo2.models

import kotlinx.serialization.Serializable

/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin, Simon Mattei, Bastien Potet
 * Date    : 13.11.2020
 */
@Serializable
data class PhoneNumber(val type: NUMBER_TYPE,
                       val number: String) {
}