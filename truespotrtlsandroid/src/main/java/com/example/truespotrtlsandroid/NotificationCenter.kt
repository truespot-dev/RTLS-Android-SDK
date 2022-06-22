package com.example.truespotrtlsandroid

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object NotificationCenter {
    private val registredObjects: HashMap<String, ArrayList<Runnable>>
    @Synchronized
    fun addFucntionForNotification(notificationName: String, r: Runnable) {
        var list = registredObjects[notificationName]
        if (list == null) {
            list = ArrayList()
            registredObjects[notificationName] = list
        }
        list.add(r)
    }

    @Synchronized
    fun removeFucntionForNotification(notificationName: String, r: Runnable) {
        val list = registredObjects[notificationName]
        list?.remove(r)
    }

    @Synchronized
    fun post(notificationName: String, data : Any) {
        val list = registredObjects[notificationName]

        if (list != null) {
//           registredObjects.set(notificationName, data )

            registredObjects[notificationName] = list
            for (r in list) r.run()
        }
    }


//    companion object {
//        //static reference for singleton
//        private var _instance: NotificationCenter? = null
//
//        //returning the reference
//        @Synchronized
//        fun defaultCenter(): NotificationCenter? {
//            if (_instance == null) _instance = NotificationCenter
//            return _instance
//        }
//    }

    //default c'tor for singleton
    init {
        registredObjects = HashMap()
    }
}