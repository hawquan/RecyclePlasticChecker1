package com.example.recycleplasticchecker.Helper

import android.os.AsyncTask
import androidx.core.util.Consumer
import java.net.InetSocketAddress
import java.net.Socket

class InternetCheck (private val consumer: Consumer): AsyncTask<Void, Void, Boolean>(){

    init{
        execute() // auto run when call this class
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        try{
            val sock = Socket()
            sock.connect(InetSocketAddress("google.com",80),1500)
            sock.close()
            return true
        }catch(e:Exception){
            return false
        }
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        consumer.accept(result)
    }

    interface Consumer{
        fun accept(isConnected:Boolean?)
    }
}