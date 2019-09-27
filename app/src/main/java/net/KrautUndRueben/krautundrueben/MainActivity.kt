package net.KrautUndRueben.krautundrueben

import android.database.MatrixCursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager


import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.Property
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity(), PermissionsListener {
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private var mapboxMap: MapboxMap? = null
    private var symbolManager: SymbolManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(
            this,
            resources.getString(R.string.mapbox_access_token)
        )
        setContentView(R.layout.activity_main)


        btn_to_own_location.setOnClickListener { it ->

            this.mapboxMap!!.cameraPosition = CameraPosition.Builder()
                .zoom(16.0)
                .tilt(20.0)
                .build()
            this.mapboxMap!!.locationComponent.cameraMode = TRACKING_GPS
        }

        val permissionsManager = PermissionsManager(this)
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapView?.onCreate(savedInstanceState)

            mapView?.getMapAsync { mapboxMap ->

                this.mapboxMap = mapboxMap
                mapboxMap.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(51.962501, 7.625561))
                    .zoom(12.0)
                    .tilt(20.0)
                    .bearing(90.0)
                    .build()
                mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->


                    symbolManager = SymbolManager(mapView!!, mapboxMap, style)
                    symbolManager!!.setIconAllowOverlap(true)
                    symbolManager!!.setTextAllowOverlap(true)

                    // createMarketImage(style)

                    style.addImage(
                        "hallo",
                        getDrawable(R.drawable.marktplatz)!!
                    )

                    val point =
                    symbolManager!!.create(
                        SymbolOptions()
                            .withLatLng(LatLng(51.962494, 7.625583))
                            .withIconImage("hallo")
                            .withIconSize(0.1.toFloat()))


                    val locationComponentOptions = LocationComponentOptions.builder(this)
                        .build()

                    val locationComponentActivationOptions = LocationComponentActivationOptions
                        .builder(this, style)
                        .locationComponentOptions(locationComponentOptions)
                        .build()

                    val locationComponent = mapboxMap.locationComponent
                    this.mapboxMap!!.locationComponent.activateLocationComponent(
                        locationComponentActivationOptions
                    )

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
                    cursor.addRow(arrayOf("0", "Murica"))
                cursor.addRow(arrayOf("1", "Murica"))
                cursor.addRow(arrayOf("2", "Murica"))
                cursor.addRow(arrayOf("3", "Murica"))
                cursor.addRow(arrayOf("4", "Murica"))

                searchView.suggestionsAdapter = SuggestionCursorAdapter(applicationContext, cursor)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu);
    }

    private fun createMarketImage(style: Style) {
        style.addImage(
            "hallo",
            getDrawable(R.drawable.marktplatz)!!
        )

        val symbolManager = SymbolManager(mapView, mapboxMap!! ,style)


        // set non-data-driven properties, such as:
        symbolManager?.iconAllowOverlap = true
        symbolManager?.iconTranslate = arrayOf(-4f, 5f)
        symbolManager?.iconRotationAlignment = Property.ICON_ROTATION_ALIGNMENT_MAP

        symbolManager!!.create(
            SymbolOptions()
                .withIconImage("hallo")
                .withLatLng(LatLng(51.962501, 7.625561))
                .withIconRotate(12.2.toFloat())
                .withIconSize(0.1.toFloat())
        )
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
            locationComponent?.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent?.renderMode = RenderMode.COMPASS
            locationComponent?.isLocationComponentEnabled = true


        } else {

            val permissionsManager = PermissionsManager(this)

            permissionsManager?.requestLocationPermissions(this)

        }
    }

}


