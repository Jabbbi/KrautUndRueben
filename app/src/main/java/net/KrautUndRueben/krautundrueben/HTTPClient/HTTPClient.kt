package net.KrautUndRueben.krautundrueben.HTTPClient

import android.content.Context
import android.widget.Toast
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import net.KrautUndRueben.krautundrueben.R


class HTTPClient(val context: Context){

    val queue = MySingleton.getInstance(context.applicationContext).requestQueue
    val url = "https://krautundrueben.net"

    fun getMarketPlaces(completationHandler: (List<MarketPlace>) -> Unit) {
        queue.add(JsonArrayRequest(Request.Method.GET, url + "/api/marketstall/", null, Response.Listener {
            val gson = Gson()
            val models = gson.fromJson(it.toString(), Array<MarketPlace>::class.java).toList()
            completationHandler(models)
        }, Response.ErrorListener {
            it -> Toast.makeText(context, "it", Toast.LENGTH_LONG).show()
        }))
    }
}
