package com.madhavsewasociety.soulserenity.mudra

import java.io.Serializable

data class Mudra(
    var name: String = "",
    var note: String = "",
    var definition: String = "",
    var release: String = "",
    var steps: String = "",
    var benefits: String = "",
    var duration: String = "",
    var imgUrl : String= ""
) : Serializable

data class Languages(
    val punjabi: Mudra = Mudra(),
    val english: Mudra = Mudra(),
    val hindi: Mudra = Mudra()
) : Serializable