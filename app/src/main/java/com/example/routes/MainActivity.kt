package com.example.routes

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setup UI and buttons
    }

    fun startListening() {
        // TODO: start SpeechRecognizer
    }

    fun stopListening() {
        // TODO: stop SpeechRecognizer
    }

    fun onResults(results: List<String>) {
        // TODO: handle speech results
    }

    fun optimizeRoute() {
        // TODO: call optimization API and generate GPX/KML
    }
}
