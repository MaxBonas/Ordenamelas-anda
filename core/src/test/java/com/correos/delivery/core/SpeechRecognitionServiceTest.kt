package com.correos.delivery.core

import android.content.Intent
import android.speech.SpeechRecognizer
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SpeechRecognitionServiceTest {

    @Test
    fun releaseDestroysRecognizer() {
        val recognizer = mock(SpeechRecognizer::class.java)
        val intent = Intent()
        val service = SpeechRecognitionService(recognizer, intent, object : SpeechRecognitionService.Callback {
            override fun onSpeechResults(results: List<String>) {}
        })

        service.release()

        verify(recognizer).destroy()
    }
}
