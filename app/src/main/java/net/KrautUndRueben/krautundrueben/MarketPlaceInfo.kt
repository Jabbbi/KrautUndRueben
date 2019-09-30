package net.KrautUndRueben.krautundrueben

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_market_place_info.*
import net.KrautUndRueben.krautundrueben.HTTPClient.MarketPlace

class MarketPlaceInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place_info)

        val market = Gson().fromJson(intent.extras!!.getString("json"), MarketPlace::class.java)

        setTitle(market.name)
        txt_description.text = Html.fromHtml(market.description)

        btn_to_store.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            val b = Bundle()
            b.putString("marketplace", Gson().toJson(market))
            i.putExtras(b)
            startActivity(i)
        }
    }
}
