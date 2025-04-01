package com.example.voiceassistant_1;

public interface OnSpeechRecognizedListener {
    void onSpeechRecognized(String text);
    void onSpeechError(String error);
    void onSpeechEnd();
}
