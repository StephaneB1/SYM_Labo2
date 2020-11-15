package com.heigvd.sym_labo2.models

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

/**
 * Project : SYM_Labo2
 * Author  : Stéphane Bottin
 * Date    : 13.11.2020
 */
@Serializable
data class Post(val id: Int,
                val title : String,
                val description : String,
                val content : String = "undefined",
                val date : String = "undefined"
) {}