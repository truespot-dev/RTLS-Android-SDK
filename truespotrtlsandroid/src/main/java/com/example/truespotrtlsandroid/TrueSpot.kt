package com.example.truespotrtlsandroid

import android.content.Context
import android.widget.Toast

class TrueSpot {
    fun showMessage(context : Context, message : String)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}