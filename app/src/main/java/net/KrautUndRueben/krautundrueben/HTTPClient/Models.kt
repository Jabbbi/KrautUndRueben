package net.KrautUndRueben.krautundrueben.HTTPClient

import android.content.Context
import android.graphics.drawable.Drawable
import com.mapbox.mapboxsdk.geometry.LatLng
import net.KrautUndRueben.krautundrueben.MainActivity
import net.KrautUndRueben.krautundrueben.R
import kotlin.contracts.contract


data class MarketPlace(
    val addressUrl: String? = null,
    val polygon: String? = null,
    val description: String? = null,
    val id: Int = 0,
    val images: Array<Image>? = null,
    val name: String? = null,
    val topics: Array<Topic>? = null
) {
    fun getLatLngs(): ArrayList<List<LatLng>> {
        val commaSeperatedValues = polygon!!.split(",")
        var innerLatLngs = ArrayList<LatLng>()
        for (i in 0..commaSeperatedValues.size -1 step 2) {
            innerLatLngs.add(LatLng(commaSeperatedValues[i].toDouble(), commaSeperatedValues[i+1].toDouble()))
        }

        if (innerLatLngs.size == 2) {
            val new = ArrayList<LatLng>()
            new.add(innerLatLngs[0])
            new.add(LatLng(innerLatLngs[1].latitude, innerLatLngs[0].longitude))

            new.add(innerLatLngs[1])
            new.add(LatLng(innerLatLngs[0].latitude, innerLatLngs[1].longitude))
            innerLatLngs = new
        }

        var latLngs = ArrayList<List<LatLng>>()
        latLngs.add(innerLatLngs)
        return latLngs
    }


    fun getDrawable(context: Context): String? {
        if(topics!!.size > 0) {
            return when(topics[0].imageFilename){
                "backwaren_04.png" -> MainActivity.IMG_BACKWAREN
                "feinkost_07.png" -> MainActivity.IMG_FEINKOST
                "fisch_11.png" -> MainActivity.IMG_FISCH
                "fleischwaren_09.png" -> MainActivity.IMG_FLEISCHWAREN
                "garten_10.png" -> MainActivity.IMG_GARTEN
                "getraenke_02.png" -> MainActivity.IMG_GETRAENKE
                "milchprodukte_08.png" -> MainActivity.IMG_MILCHPRODUKTE
                "obst_03.png" -> MainActivity.IMG_OBST
                "suesswaren_05.png" -> MainActivity.IMG_SUESWAREN
                else -> MainActivity.IMG_SONSTIGE
            }
        }
        return MainActivity.IMG_SONSTIGE
    }
}

data class Image(
    val id: Int? = null,
    val imageUrl: String? = null,
    val priotiy: String? = null,
    val marketstall: Int? = null,
    val description: String? = null
)

data class Topic(
    val id: Int = 0,
    val title: String? = null,
    val imageFilename: String? = null,
    val priority: Int? = null,
    val active: Boolean = false
)