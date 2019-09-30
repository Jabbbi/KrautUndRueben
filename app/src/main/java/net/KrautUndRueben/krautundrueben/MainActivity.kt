package net.KrautUndRueben.krautundrueben

import android.content.Intent
import android.database.MatrixCursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.CameraMode.TRACKING_GPS
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style


import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import com.mapbox.mapboxsdk.plugins.annotation.*
import net.KrautUndRueben.krautundrueben.HTTPClient.HTTPClient
import net.KrautUndRueben.krautundrueben.HTTPClient.MarketPlace
import android.view.MenuItem
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity(), PermissionsListener
    { companion object {
        val IMG_BACKWAREN = "IMG_BACKWAREN"
        val IMG_FEINKOST = "IMG_FEINKOST"
        val IMG_FISCH = "IMG_FIISCH"
        val IMG_FLEISCHWAREN = "IMG_FLEISCHWAREN"
        val IMG_GARTEN = "IMG_GARTEN"
        val IMG_GETRAENKE = "IMG_GETRAENKE"
        val IMG_MILCHPRODUKTE = "IMG_MILCHPRODUKTE"
        val IMG_OBST = "IMG_OBST"
        val IMG_SONSTIGE = "IMG_SONSTIGE"
        val IMG_SUESWAREN = "IMG_SUESWAREN"
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    private var mapboxMap: MapboxMap? = null
    private var symbolManager: SymbolManager? = null
    private var fillManager: FillManager? = null
    private var marketPlaces: List<MarketPlace>? = null
    private var symbolLatLngToMarketPlace: HashMap<LatLng, MarketPlace> = HashMap();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HTTPClient(this).getMarketPlaces {
            this.marketPlaces = marketPlaces
        }

        var marketplace: MarketPlace? = null
        if (intent.extras != null) {
            marketplace = Gson().fromJson(intent.extras!!.getString("marketplace"), MarketPlace::class.java)
        }



        Mapbox.getInstance(
            this,
            resources.getString(R.string.mapbox_access_token)
        )
        setContentView(R.layout.activity_main)

        btn_to_own_location.setOnClickListener {
            this.mapboxMap!!.cameraPosition = CameraPosition.Builder()
                .zoom(16.0)
                .tilt(20.0)
                .build()
            this.mapboxMap!!.locationComponent.cameraMode = TRACKING_GPS
        }

        val permissionsManager = PermissionsManager(this)
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            map?.onCreate(savedInstanceState)

            map?.getMapAsync { mapboxMap ->

                this.mapboxMap = mapboxMap
                mapboxMap.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(51.962501, 7.625561))
                    .zoom(16.0)
                    .tilt(22.0)
                    .bearing(270.0)
                    .build()
                mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->

                    symbolManager = SymbolManager(map!!, mapboxMap, style)
                    symbolManager!!.setIconAllowOverlap(true)
                    symbolManager!!.setTextAllowOverlap(true)


                    style.addImage(
                        IMG_BACKWAREN,
                        getDrawable(R.drawable.backwaren_04)!!
                    )
                    style.addImage(
                        IMG_FEINKOST,
                        getDrawable(R.drawable.feinkost_07)!!
                    )
                    style.addImage(
                        IMG_FISCH,
                        getDrawable(R.drawable.fisch_11)!!
                    )
                    style.addImage(
                        IMG_FLEISCHWAREN,
                        getDrawable(R.drawable.fleischwaren_09)!!
                    )
                    style.addImage(
                        IMG_GETRAENKE,
                        getDrawable(R.drawable.getraenke_02)!!
                    )
                    style.addImage(
                        IMG_MILCHPRODUKTE,
                    getDrawable(R.drawable.milchprodukte_08)!!
                    )
                    style.addImage(
                        IMG_SONSTIGE,
                        getDrawable(R.drawable.sonstiges_06)!!
                    )
                    style.addImage(
                        IMG_SUESWAREN,
                        getDrawable(R.drawable.suesswaren_05)!!
                    )
                    style.addImage(
                        IMG_OBST,
                        getDrawable(R.drawable.obst_03)!!
                    )
                    style.addImage(
                        IMG_GARTEN,
                        getDrawable(R.drawable.garten_10)!!
                    )

                    val locationComponentOptions = LocationComponentOptions.builder(this)
                        .build()

                    val locationComponentActivationOptions = LocationComponentActivationOptions
                        .builder(this, style)
                        .locationComponentOptions(locationComponentOptions)
                        .build()

                    this.mapboxMap!!.locationComponent.activateLocationComponent(
                        locationComponentActivationOptions
                    )

                    fillManager = FillManager(map!!, mapboxMap, style)

                    symbolManager!!.addClickListener {

                        val marketplace = symbolLatLngToMarketPlace[it.latLng]

                        txt_map_name.visibility = View.VISIBLE
                        btn_back.visibility = View.VISIBLE
                        btn_get_info.visibility = View.VISIBLE
                        txt_map_name.text = marketplace!!.name
                        txt_map_name.bringToFront()

                        btn_get_info.setOnClickListener {
                            txt_map_name.visibility = View.INVISIBLE
                            btn_back.visibility = View.INVISIBLE
                            btn_get_info.visibility = View.INVISIBLE

                            val i = Intent(this, MarketPlaceInfo::class.java)
                            val b = Bundle()
                            b.putString("json", Gson().toJson(marketplace))
                            i.putExtras(b)
                            this.startActivity(i)
                        }

                        btn_back.setOnClickListener {
                            txt_map_name.visibility = View.INVISIBLE
                            btn_back.visibility = View.INVISIBLE
                            btn_get_info.visibility = View.INVISIBLE
                        }
                    }

                    HTTPClient(this).getMarketPlaces {
                        marketPlaces = it
                        for (marketPlace in it) {
                            val stuff = marketPlace.getLatLngs()

                            fillManager!!.create(
                                FillOptions()
                                    .withLatLngs(stuff)
                                    .withFillColor("rgba(255,0,0,0.1)")
                                    .withFillOutlineColor("rgba(255,0,0,1)")
                            )
                            Log.d("Coards",stuff.toString())

                            val centerCords = badCenterOfLatNng(stuff[0])

                            symbolLatLngToMarketPlace[centerCords] = marketPlace

                            symbolManager!!.create(
                                SymbolOptions()
                                    .withIconSize(0.1.toFloat())
                                    .withIconImage(marketPlace.getDrawable(this))
                                    .withLatLng(centerCords)
                            )
                        }
                    }

                    if(marketplace != null) {
                        mapboxMap.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(badCenterOfLatNng(marketplace!!.getLatLngs()[0])))
                            .zoom(20.0)
                            .tilt(27.0)
                            .bearing(270.0)
                            .build()
                    }

                    enableLocationComponent(style)
                }
            }

        } else {
            permissionsManager.requestLocationPermissions(this);
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {

                val cursor = MatrixCursor(arrayOf("_id", "suggest_text_1"));
                for (element in marketPlaces!!.iterator()) {
                    if (element.name!!.contains(s, ignoreCase = true)) {
                        cursor.addRow(arrayOf("0", Gson().toJson(element)))
                    }
                }

                val sca = SuggestionCursorAdapter(applicationContext, cursor)
                searchView.suggestionsAdapter = sca
                return true
            }
        })

        return super.onCreateOptionsMenu(menu);
    }

    /*

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.checkable_menu -> {
                val isChecked = !item.isChecked()
                item.setChecked(isChecked)
                return true
            }
            else -> return false
        }
    }

     */

    private fun badCenterOfLatNng(latngs: List<LatLng>): LatLng {

        var long_sum = 0.0
        var lat_sum = 0.0

        for (element in latngs) {
            lat_sum += element.latitude
            long_sum += element.longitude
        }
        var lat_avg = lat_sum / latngs.size
        var long_avg = long_sum / latngs.size
        return LatLng(lat_avg, long_avg)
    }

    private fun enableLocationComponent(style: Style) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            val locationComponent = this.mapboxMap!!.locationComponent

            // Activate with a built LocationComponentActivationOptions object
            locationComponent?.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())

            // Enable to make component visible
            locationComponent?.isLocationComponentEnabled = true

            // Set the component's camera mode

            // Set the component's render mode
            locationComponent?.renderMode = RenderMode.COMPASS
            locationComponent?.isLocationComponentEnabled = true

        } else {
            val permissionsManager = PermissionsManager(this)

            permissionsManager?.requestLocationPermissions(this)

        }
    }
}


