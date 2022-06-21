package com.example.truespotrtlsandroid.models

import com.google.gson.annotations.SerializedName

data class BeaconRegion(

    @SerializedName("proximityUUID")
    var proximityUUID: String? = null,


    @SerializedName("identifier")
    var identifier: String? = null

)