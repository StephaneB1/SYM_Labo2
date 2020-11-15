package com.heigvd.sym_labo2

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
data class Author(val id: Int,
                  val first_name : String,
                  val last_name : String,
                  val email : String = "undefined",
                  val birthday : String = "undefined",
                  val added : String = "undefined",
                  val posts : ArrayList<Post> = ArrayList()
) {}