package com.example.truespotrtlsandroid.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class PairRequestBody(
    @field:JsonProperty("assetIdentifier") var assetIdentifier: String,
    @field:JsonProperty("assetType") var assetType: String,
) : BaseJSONModel()

