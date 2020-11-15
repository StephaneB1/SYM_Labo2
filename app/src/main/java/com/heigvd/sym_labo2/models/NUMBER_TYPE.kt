package com.heigvd.sym_labo2.models

import kotlinx.serialization.Serializable

/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin
 * Date    : 13.11.2020
 */
@Serializable
enum class NUMBER_TYPE {
    WORK,
    HOME,
    MOBILE
}