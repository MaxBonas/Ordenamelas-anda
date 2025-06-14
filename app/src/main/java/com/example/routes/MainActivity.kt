package com.example.routes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.location.LocationManager
import android.location.Geocoder
import android.content.Context
import androidx.core.content.FileProvider
import com.example.routes.BuildConfig
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.correos.delivery.core.SpeechRecognitionService
import com.correos.delivery.export.GpxExporter
import com.correos.delivery.repository.AddressRepository
import com.correos.delivery.api.RouteOptimizer
import com.example.routes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SpeechRecognitionService.Callback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var speechService: SpeechRecognitionService
    private lateinit var adapter: AddressAdapter
    private val addresses = mutableListOf<String>()
    private val REQUEST_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AddressAdapter(addresses)
        binding.addressList.layoutManager = LinearLayoutManager(this)
        binding.addressList.adapter = adapter

        speechService = SpeechRecognitionService(this, this)

        checkPermissions()

        binding.btnPushToTalk.setOnClickListener { startListening() }
        binding.btnDone.setOnClickListener { optimizeRoute() }
    }

    fun startListening() {
        try {
            speechService.startListening()
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to start speech recognition", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopListening() {
        try {
            speechService.stopListening()
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to stop speech recognition", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        val needed = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            needed.add(Manifest.permission.RECORD_AUDIO)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            needed.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            needed.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            var granted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            if (!granted) {
                AlertDialog.Builder(this)
                    .setTitle("Permissions required")
                    .setMessage("The app needs microphone, location and storage permissions to work")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    override fun onSpeechResults(results: List<String>) {
        onResults(results)
    }

    fun onResults(results: List<String>) {
        val address = results.firstOrNull() ?: return
        adapter.addAddress(address)
    }

    fun optimizeRoute() {
        if (addresses.isEmpty()) {
            Toast.makeText(this, "No addresses to optimize", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtain current location using LocationManager
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (location == null) {
            Toast.makeText(this, "Unable to obtain current location", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert spoken addresses into Address objects with coordinates
        val geocoder = Geocoder(this)
        val repo = AddressRepository()
        for (a in addresses) {
            val parts = a.split(",").map { it.trim() }
            val result = try { geocoder.getFromLocationName(a, 1) } catch (e: Exception) { null }
            val first = result?.firstOrNull()
            repo.add(
                Address(
                    postalCode = parts.getOrNull(0) ?: "",
                    street = parts.getOrNull(1) ?: "",
                    number = parts.getOrNull(2) ?: "",
                    latitude = first?.latitude ?: 0.0,
                    longitude = first?.longitude ?: 0.0
                )
            )
        }

        // Optimize the list of stops
        val optimizer = RouteOptimizer()
        val ordered = optimizer.optimize(repo.getAll())

        val exporter = GpxExporter()
        val gpxFile = try {
            exporter.export(ordered)
        } catch (e: Exception) {
            Toast.makeText(this, "Error writing GPX file", Toast.LENGTH_SHORT).show()
            null
        }

        if (gpxFile != null) {
            val uri = FileProvider.getUriForFile(
                this,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                gpxFile
            )
            launchNavigation(uri)
        } else {
            Toast.makeText(this, "Failed to export route", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open the provided GPX file in an external navigation app such as Google Maps.
     */
    fun launchNavigation(gpxUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(gpxUri, "application/gpx+xml")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app found to open GPX", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechService.release()
    }
}
