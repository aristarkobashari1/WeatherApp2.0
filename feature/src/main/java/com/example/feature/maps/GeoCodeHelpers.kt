package com.example.feature.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.model.Coord
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException




class GeoCodeHelpers(
    private val activity: Activity,
    private val fusedClientLocation: FusedLocationProviderClient?=null,
    private val onCityNameObtained: ((String, Coord) -> Unit?)? = null
) {

    private val geocoder = Geocoder(activity)
    @SuppressLint("MissingPermission")
    fun requestSingleLocation() {
        fusedClientLocation?.lastLocation?.addOnSuccessListener {
            val cityName =getCityName(it.latitude,it.longitude)
            onCityNameObtained?.let { it1 -> it1(cityName,Coord(it.latitude,it.longitude)) }
        }
    }

    fun getLocationFromAddress(strAddress: String): Coord? {
        try {
            val addresses: List<Address>? = geocoder.getFromLocationName(strAddress, 1)

            if (addresses.isNullOrEmpty()) return null

            val location = addresses[0]

            return Coord(location.latitude, location.longitude)
        } catch (ex: IOException) {
            // Handle IOException, e.g., network or I/O errors
            ex.printStackTrace()
            return null
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(activity)
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return addresses?.firstOrNull()?.locality ?: "Unknown City"
    }

    fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
        private const val LOCATION_UPDATE_FASTEST_INTERVAL = 5000L // 5 seconds
    }
}
