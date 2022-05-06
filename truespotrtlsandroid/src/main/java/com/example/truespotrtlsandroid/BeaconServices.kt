package com.example.truespotrtlsandroid

import com.example.truespotrtlsandroid.models.Credentials

class BeaconServices {
    fun authenticate()
    {
        if(Credentials.clientSecret != null && Credentials.tenantId != null)
        {
            val secret = Credentials.clientSecret
            val tenantId = Credentials.tenantId

        }
        else
        {
            return
        }
    }
}
class API{
    //DEV
    val authURL = "https://authprovider-d-us-c-api.azurewebsites.net/"

    //PROD
    // val authURL = "https://auth.truespot.com/"

    //DEV
    val RTLSBaseURL = "https://rtls-d-us-c-api.azurewebsites.net/"

    //PROD
    //val RTLSBaseURL = "https://rtls.truespot.com/"

    class Endpoint{
        val authorization = "api/api-authorizations"
        val trackingDevices = "api/tracking-devices"
        val applications = "api/applications"
    }
}