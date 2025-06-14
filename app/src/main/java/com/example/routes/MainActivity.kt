package com.example.routes

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.correos.delivery.core.SpeechRecognitionService
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
        Toast.makeText(this, "Optimize route not implemented", Toast.LENGTH_SHORT).show()
    }
}
