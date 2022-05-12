package com.example.truespotrtlsandroid.models

import com.google.gson.annotations.SerializedName

data class Authen(
    @SerializedName("jwt")
    var jwt: String? = null

)