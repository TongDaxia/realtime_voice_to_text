package com.tyg.speech.service;

import com.tyg.speech.rpc.PythonSpeechClient;
import com.tyg.speech.util.LogUtils;

public class SpeechRecognitionService {
    private final PythonSpeechClient pythonClient;

    public SpeechRecognitionService(PythonSpeechClient pythonClient) {
        this.pythonClient = pythonClient;
    }

    public String recognize(byte[] audioData) {
        try {
            long startTime = System.currentTimeMillis();
            String result = pythonClient.recognize(audioData,16000);
            long duration = System.currentTimeMillis() - startTime;
            LogUtils.logRecognitionTime(duration);
            return result;
        } catch (Exception e) {
            LogUtils.logError("Recognition failed: " + e.getMessage());
            return null;
        }
    }
}