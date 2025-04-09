package com.example.voiceassistant_1;
// change this to match your app package structure

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiHelper {
    private static final String API_KEY = "AIzaSyAi0bWB665kGhvlF7y9y26wvCYs-bIVwKg"; // 🔐 Replace with your actual Gemini API key
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    public static void fetchFallbackResponse(String userQuery, GeminiCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String prompt = "The user said: \"" + userQuery + "\". They were trying to use a voice assistant. "
                + "Suggest a helpful action they might be trying to do. Keep it under 30 words.";

        JSONObject requestBody = new JSONObject();
        try {
            JSONObject part = new JSONObject();
            part.put("text", prompt);

            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            requestBody.put("contents", contents);
        } catch (JSONException e) {
            callback.onResponse("Sorry, I couldn't understand your request.");
            return;
        }

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("GeminiAPI", "API Call Failed", e);
                callback.onResponse("I couldn’t connect to the internet.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onResponse("I couldn't think of anything helpful.");
                    return;
                }

                String resBody = response.body().string();
                try {
                    JSONObject json = new JSONObject(resBody);
                    JSONArray candidates = json.getJSONArray("candidates");
                    JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    String text = parts.getJSONObject(0).getString("text");
                    callback.onResponse(text.trim());
                } catch (JSONException e) {
                    callback.onResponse("Sorry, something went wrong.");
                }
            }
        });
    }

    public interface GeminiCallback {
        void onResponse(String text);
    }
}
