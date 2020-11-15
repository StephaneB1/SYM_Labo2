package com.heigvd.sym_labo2.models

import com.heigvd.sym_labo2.models.Post
import kotlinx.serialization.Serializable
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