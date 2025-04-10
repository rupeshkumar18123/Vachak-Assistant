package com.example.voiceassistant_1;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class GeminiHelper {
    private final ChatFutures chat;
    private final Handler mainHandler;

    public GeminiHelper(Context context) {
        // Initialize with latest model (gemini-1.5-flash is fastest as of June 2024)
        GenerativeModel model = new GenerativeModel(
                "gemini-1.5-pro-latest",
                ""  // Replace with your actual API key
        );

        this.chat = GenerativeModelFutures.from(model).startChat();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getAIResponse(String query, AICallback callback) {
        Content content = new Content.Builder()
                .addText(query)
                .build();

        ListenableFuture<GenerateContentResponse> response = chat.sendMessage(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String text = result.getText();
                mainHandler.post(() -> callback.onSuccess(text));
            }

            @Override
            public void onFailure(Throwable t) {
                mainHandler.post(() -> callback.onError("Error: " + t.getMessage()));
            }
        }, Runnable::run);
    }

    public interface AICallback {
        void onSuccess(String response);
        void onError(String error);
    }
}