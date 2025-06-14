package com.example.routes

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.routes.BuildConfig
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.correos.delivery.core.SpeechRecognitionService
import com.correos.delivery.export.GpxExporter
import com.correos.delivery.repository.AddressRepository
import com.example.routes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SpeechRecognitionService.Callback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var speechService: SpeechRecognitionService
    private lateinit var adapter: AddressAdapter
    private val addresses = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AddressAdapter(addresses)
        binding.addressList.layoutManager = LinearLayoutManager(this)
        binding.addressList.adapter = adapter

        speechService = SpeechRecognitionService(this, this)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)

        binding.btnPushToTalk.setOnClickListener { startListening() }
        binding.btnDone.setOnClickListener { optimizeRoute() }
    }

    fun startListening() {
        speechService.startListening()
    }

    fun stopListening() {
        speechService.stopListening()
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

        val repo = AddressRepository()
        for (a in addresses) {
            val parts = a.split(",").map { it.trim() }
            repo.add(
                Address(
                    postalCode = parts.getOrNull(0) ?: "",
                    street = parts.getOrNull(1) ?: "",
                    number = parts.getOrNull(2) ?: "",
                    latitude = 0.0,
                    longitude = 0.0
                )
            )
        }

        val exporter = GpxExporter()
        val gpxFile = try {
            exporter.export(repo.getAll())
        } catch (e: Exception) {
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
}
