package com.correos.delivery.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Service wrapper around {@link SpeechRecognizer} used to capture spoken
 * addresses. The results of speech recognition are forwarded to a callback
 * provided by the caller.
 */
public class SpeechRecognitionService {

    /** Callback that receives final speech recognition results. */
    public interface Callback {
        void onSpeechResults(List<String> results);
    }

    private final SpeechRecognizer recognizer;
    private final Intent recognizerIntent;
    private final Callback callback;

    public SpeechRecognitionService(Context context, Callback callback) {
        this.callback = callback;
        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        recognizer.setRecognitionListener(new Listener());
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }

    /** Start listening for speech input. */
    public void startListening() {
        recognizer.startListening(recognizerIntent);
    }

    /** Stop listening for speech input. */
    public void stopListening() {
        recognizer.stopListening();
    }

    private class Listener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) { }

        @Override
        public void onBeginningOfSpeech() { }

        @Override
        public void onRmsChanged(float rmsdB) { }

        @Override
        public void onBufferReceived(byte[] buffer) { }

        @Override
        public void onEndOfSpeech() { }

        @Override
        public void onError(int error) { }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION);
            if (callback != null && matches != null) {
                callback.onSpeechResults(matches);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) { }

        @Override
        public void onEvent(int eventType, Bundle params) { }
    }
}

