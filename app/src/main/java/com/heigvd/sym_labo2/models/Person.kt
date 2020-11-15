package com.heigvd.sym_labo2.models

import kotlinx.serialization.Serializable

/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin, Simon Mattei, Bastien Potet
 * Date    : 13.11.2020
 */
@Serializable
data class Person(val name: String,
                  val firstName: String,
                  val middleName: String,
                  val gender: GENDER,
                  val numbers : ArrayList<PhoneNumber>) {
}