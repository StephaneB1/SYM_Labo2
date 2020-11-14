package com.heigvd.sym_labo2

import kotlinx.serialization.Serializable

/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin
 * Date    : 13.11.2020
 */
@Serializable
data class PhoneNumber(val type: NUMBER_TYPE,
                       val number: String) {
}