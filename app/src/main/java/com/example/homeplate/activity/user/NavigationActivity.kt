package com.example.homeplate.activity.user

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.KeyEvent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.homeplate.R
import com.example.homeplate.model.RestaurantItem
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class NavigationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    private val REQUEST_CODE = 1001
    private lateinit var locationRequest : LocationRequest
    private lateinit var locationCallback : LocationCallback

    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var db: FirebaseFirestore
    private lateinit var resEmail :String
    private lateinit var currentLocation: Location
    private lateinit var mapFragment :SupportMapFragment
    private var alreadyStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        //setup firebase firestore and get restaurant email
        db = FirebaseFirestore.getInstance()
        resEmail = intent.getStringExtra("restaurant email")

        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object :LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                //Log.d("DEBUG", "old loc: ${LatLng(lastLocation.latitude, lastLocation.longitude)}")
                //Log.d("DEBUG", "new loc: ${LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)}")
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation
                getNavigation()
            }
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions,
                REQUEST_CODE
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        Log.d("DEBUG", "MAP LOADED")
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest()
        }
    }

    private fun getLatLong(address: String) : LatLng{
        val geocoder = Geocoder(this)
        var addresses : List<Address> = geocoder.getFromLocationName(address, 1)
        val add: Address = addresses.get(0)
        return LatLng(add.latitude,add.longitude)
    }

    private fun getNavigation() {
        Log.d("LOG", "NAVIGATION STARTED")
        db.collection("owners").document(resEmail).get().addOnCompleteListener {task ->
            val restaurant = task.result!!.toObject(RestaurantItem::class.java)

            val latLngOrigin = LatLng(currentLocation.latitude,currentLocation.longitude)
            val latLngDestination = getLatLong(restaurant!!.address)

            this.googleMap!!.clear()
            this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Current Location"))
            this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination).title("Destination"))
            this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))

            val path: MutableList<List<LatLng>> = ArrayList()
            val urlDirections =
                "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin="+latLngOrigin.latitude+","+latLngOrigin.longitude+
                        "&destination="+latLngDestination.latitude+","+latLngDestination.longitude+
                        "&key=AIzaSyCJ9BqyIzbXy_Eho7D9k9f889fMbhgb76w"

            val directionsRequest = object : StringRequest(
                Request.Method.GET, urlDirections, Response.Listener<String> {
                        response ->
                    val jsonResponse = JSONObject(response)
                    // Get routes
                    val routes = jsonResponse.getJSONArray("routes")
                    val legs = routes.getJSONObject(0).getJSONArray("legs")
                    val steps = legs.getJSONObject(0).getJSONArray("steps")
                    for (i in 0 until steps.length()) {
                        val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                        path.add(PolyUtil.decode(points))
                    }
                    for (i in 0 until path.size) {
                        this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                    }
                }, Response.ErrorListener {}){}
            val requestQueue = Volley.newRequestQueue(this@NavigationActivity)
            requestQueue.add(directionsRequest)
        }
        alreadyStarted = true
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 1000
        locationRequest.numUpdates = 5
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d("DEBUG", "LOCATION UPDATES STARTED")
        }
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        this.fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE) {
            if(grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}