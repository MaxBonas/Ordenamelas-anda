package com.example.routes

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.correos.delivery.core.SpeechRecognitionService

class MainActivity : AppCompatActivity(), SpeechRecognitionService.Callback {

    private lateinit var speechService: SpeechRecognitionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setup UI and buttons
        speechService = SpeechRecognitionService(this, this)
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
        // TODO: handle speech results
    }

    fun optimizeRoute() {
        // TODO: call optimization API and generate GPX/KML
    }
}
