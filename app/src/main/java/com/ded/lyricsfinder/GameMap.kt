package com.ded.lyricsfinder

import android.app.Dialog
import kotlin.collections.ArrayList
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.android.synthetic.main.activity_game_map.*
import android.content.Context

import android.location.LocationManager
import android.net.ConnectivityManager

import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_loot_inspector.*
import kotlin.random.Random

class GameMap : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val PERMISSION_CODE: Int = 1000
    }

    private lateinit var mMap: GoogleMap

    private var latitude:Double = 0.toDouble()
    private var longitude:Double = 0.toDouble()
    private var bearing:Float = 0f

    private lateinit var mLastLocation: Location
    private var mMarker:Marker? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    // Corners of the play area
    private val maxLat = 51.621181
    private val minLong = -3.885353
    private val maxLong = -3.873420
    private val minLat = 51.617756

    // SONGS AND LYRICS
    private val numOfSongsNLyrics = 4
    lateinit var authors: ArrayList<String>
    lateinit var names: ArrayList<String>

    var songIndexes = ArrayList<Int>(numOfSongsNLyrics)
    var songsLyrics = Array(20) { Array(numOfSongsNLyrics) { "" } }


    // MARKERS
    var markers = ArrayList<Marker>()
    var collectedLyrics = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Request runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        isLocationOn()
        isNetworkOn()

        buildLocationRequest()
        buildLocationCallback()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        (this.application as DataFinder).clear()
        getSongs()

        lyrics_holder_btn.setOnClickListener {
            val lyricsHolder = Intent(this, LyricsHolder::class.java)
            lyricsHolder.putExtra(getString(R.string.lyrics), collectedLyrics)
            startActivity(lyricsHolder)
        }

        inventory_btn.setOnClickListener {
            val inventory = Intent(this, Inventory::class.java)
            startActivity(inventory)
        }


        collect_btn.setOnClickListener {

            isLocationOn()
            isNetworkOn()
            buildLocationCallback()

            var collected = false
            for (i in 0 until markers.size) {

                val distance = FloatArray(1)
                Location.distanceBetween(
                    latitude, longitude, markers[i].position.latitude, markers[i].position.longitude, distance)

                /*
                 * Checking distance between points and finding nearest point within the threshold
                 */
                if (distance[0] < 30 && (markers[i].snippet != "removed")) {
                    collectedLyrics.add(markers[i].tag.toString())
                    markers[i].snippet = "removed"
                    markers[i].remove()
                    collected = true
                }
            }
            if (collected) {

                val collectedDialog = Dialog(this)
                collectedDialog.setContentView(R.layout.activity_loot_inspector)
                collectedDialog.lyric_display.text = collectedLyrics.last().substringAfter(';').substringBefore(':')

                if (collectedLyrics.last().substringAfter(':') == "Easy") {
                    collectedDialog.diff_circle_icon.setImageResource(R.drawable.greensmallnoteicon)
                } else if (collectedLyrics.last().substringAfter(':') == "Medium") {
                    collectedDialog.diff_circle_icon.setImageResource(R.drawable.yellowsmallnoteicon)
                } else {
                    collectedDialog.diff_circle_icon.setImageResource(R.drawable.redsmallnoteicon)
                }

                /*
                 * Loot window and money assignment (if found any)
                 */
                val rngCoin = Random.nextInt(0, 100)
                if (rngCoin < 20) {
                    collectedDialog.coins.text = " "
                    collectedDialog.coin_img.setImageResource(R.drawable.transparrentsquare)
                } else if (rngCoin in 20..60) {
                    collectedDialog.coins.text = "10 coins"
                    val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putInt("coins", prefs.getInt("coins", 0) + 10)
                    editor.apply()
                } else {
                    collectedDialog.coins.text = "50 coins"
                    val prefs = getSharedPreferences("gameResources", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putInt("coins", prefs.getInt("coins", 0) + 50)
                    editor.apply()
                }

                collectedDialog.take_btn.setOnClickListener {
                    collectedDialog.dismiss()
                }
                collectedDialog.show()

            } else {
                var nothingMessage = AlertDialog.Builder(this)
                nothingMessage.setMessage("There is nothing to collect. You need to be closer to the marker.")
                nothingMessage.setNegativeButton("OK") { _, _ -> }

                val alertDialog : AlertDialog = nothingMessage.create()
                alertDialog.show()

            }
        }
    }

    /**
       Getting and updating last location and camera
     */
    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(myLocation: LocationResult?) {
                // Getting last location
                mLastLocation = myLocation!!.locations.get(myLocation.locations.size - 1)

                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                bearing = mLastLocation.bearing

                val latLng = LatLng(latitude, longitude)

                val cameraPosition = CameraPosition.Builder()
                    .target(latLng)
                    .zoom(18.5f)
                    .bearing(bearing)
                    .tilt(58f)
                    .build()

                // move camera to the location
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


            }
        }
    }

    /**
    Building location request
     */
    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    private fun checkLocationPermission(): Boolean {
        var result = false
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_CODE)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_CODE)
                result = false
            }
        } else {
            result = true
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if(checkLocationPermission()) {
                            mMap.isMyLocationEnabled = true
                        }
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        } else {
            mMap.isMyLocationEnabled = true
        }


        // Setting up markers
        var counter = 0
        for (i in 0 until songsLyrics.size) {
            if (!(songsLyrics[i][0].isEmpty())) {

                for (j in 0 until numOfSongsNLyrics) {
                    // Coordinates
                    val randLat = minLat + Math.random() * (maxLat - minLat)
                    val randLong = minLong + Math.random() * (maxLong - minLong)
                    val randPoint = LatLng(randLat, randLong)

                    var title: String
                    var colour: Int
                    if (counter == 0 || counter == 1) {
                        title = "Easy"
                        colour = R.drawable.greensmallnotemarker
                    } else {
                        if (counter == 2) {
                            title = "Medium"
                            colour = R.drawable.yellowsmallnotemarker
                        } else {
                            title = "Hard"
                            colour = R.drawable.redsmallnotemarker
                        }
                    }
                    val marker = mMap.addMarker(MarkerOptions()
                        .position(randPoint)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromResource(colour))
                        .snippet(randPoint.toString()))

                    marker.tag = authors[i] + "," + names[i] + ";" + songsLyrics[i][j] + ":" + marker.title
                    markers.add(marker)
                }
                counter ++
            }
        }

    }

    /**
     * Checks if location is ON
     */
    private fun isLocationOn(): Boolean {
        var isOn = false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val messageGPS = AlertDialog.Builder(this)
            messageGPS.setMessage("Location is diabled")
            messageGPS.setNegativeButton("Close") { _, _ -> }
            messageGPS.setPositiveButton("Location Settings") { _, _ ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            val alertDialog : AlertDialog = messageGPS.create()
            alertDialog.show()

        } else isOn = true

        return isOn
    }

    /**
     * Checks if network (internet connection) is ON
     */
    private fun isNetworkOn(): Boolean {
        var isOn = true
        val networkManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(networkManager.activeNetwork == null) {
            val messageNetwork = AlertDialog.Builder(this)
            messageNetwork.setMessage(getString(R.string.netDis))
            messageNetwork.setNegativeButton("Close") { _, _ -> }
            messageNetwork.setPositiveButton(" Network Settings") { _, _ ->
                startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
            }
            val alertDialog : AlertDialog = messageNetwork.create()
            alertDialog.show()
            isOn = false
        }
        return isOn
    }

    /**
     * Getting songs from txt files and setting them up on the map
     */
    private fun getSongs() {

        var songs: List<String> = applicationContext.assets.open(intent.getStringExtra("selectedMode") + ".txt").
            bufferedReader().useLines { it.toList() }
        val songsArr: ArrayList<String> = ArrayList(songs)


        authors = ArrayList(songs)
        names = ArrayList(songs)

        for (i in 0..(authors.size-1)) {
            authors[i] = authors[i].substringBefore("(").replace("_", " ")
            names[i] = names[i].substringAfter("(").replace("_", " ").replace(").txt", "")
        }


        var i = numOfSongsNLyrics
        while (i > 0) {
            var randSongIndex = (0 until songsArr.size).random()
            if (!songIndexes.contains(randSongIndex)) {
                songIndexes.add(randSongIndex)

                var tempLyrics = ArrayList(applicationContext.assets.open(
                    intent.getStringExtra("selectedMode") + "/" + songsArr[randSongIndex]).bufferedReader().useLines { it.toList() })

                for (j in 0 until numOfSongsNLyrics) {
                    val selectedLyric = tempLyrics.random()
                    tempLyrics.remove(selectedLyric)
                    songsLyrics[randSongIndex][j] = selectedLyric

                }
            } else {
                i += 1
            }
            i -= 1
        }

    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()

    }
}
