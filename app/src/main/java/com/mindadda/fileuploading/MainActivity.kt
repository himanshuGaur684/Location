package com.mindadda.fileuploading

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient





    private val callback= object:LocationCallback(){
        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }

        override fun onLocationResult(result: LocationResult?) {
            val lastLocation= result?.lastLocation
            Log.d("TAG", "onLocationResult: ${lastLocation?.longitude.toString()}")
            findViewById<TextView>(R.id.longitude).text="Longitude : "+lastLocation?.longitude.toString()
            findViewById<TextView>(R.id.latitude).text="Latitude : "+lastLocation?.latitude.toString()
            super.onLocationResult(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        onGPS()
        findViewById<Button>(R.id.button).setOnClickListener {
           fetchLocation()
        }

    }


    fun onGPS() {

        Log.d("TAG", "onGPS: ${isLocationEnabled()}")

        if (!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            fetchLocation()
        }


    }

    private fun fetchLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    200
                )
                return
            }else{
                requestLocation()
            }


        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        Log.d("TAG", "requestLocation: ")
        val requestLocation= LocationRequest()
        requestLocation.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        requestLocation.interval = 0
        requestLocation.fastestInterval = 0
        requestLocation.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            requestLocation,callback, Looper.myLooper()
        )

    }


    fun isLocationEnabled(): Boolean {
        val locationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}




