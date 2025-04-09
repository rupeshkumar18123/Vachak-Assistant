package com.example.voiceassistant_1;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.TextView;
import android.provider.Settings;


public class MainActivity extends AppCompatActivity implements OnSpeechRecognizedListener, WeatherHelper.WeatherCallback {

    private static final int REQ_RECORD_AUDIO = 1;
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 102;

    private LottieAnimationView micButton;
    private FlashLight flashLight; // ✅ ADDED

    private VideoView videoView;
    private TextView responseText;

    private SpeechListener speechListener;
    private TexttoSpeech texttoSpeech;
    private AppLaunch appLaunch;
    private CallHelper callHelper;
    private WeatherHelper weatherHelper;
    private NLPIntentMatcher nlpIntentMatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nlpIntentMatcher = new NLPIntentMatcher(IntentWordList.intentWords);
        videoView = findViewById(R.id.videoView);
        micButton = findViewById(R.id.micButton);
        responseText = findViewById(R.id.responseText);
//        NLPIntentMatcher nlpMatcher = new NLPIntentMatcher();
//        nlpIntentMatcher = new NLPIntentMatcher();
//        nlpIntentMatcher = new NLPIntentMatcher(IntentWordList.intentWords);



        // Background video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
        });

        if(!isNotiaccessenable()){
            reqnotiaccess();
        }

        // Request necessary permissions
        requestPermissions();

        // Initialize helpers
        texttoSpeech = new TexttoSpeech(this);
        NotificationRead.init(texttoSpeech);

        appLaunch = new AppLaunch(this, texttoSpeech);
        callHelper = new CallHelper(this);
        weatherHelper = new WeatherHelper(this);
//        flashLight = new FlashLight(this)
        flashLight = new FlashLight(this, texttoSpeech); // ✅ Correct


        // Welcome message
        texttoSpeech.speak("Hello! What can I help you with?");

        // Initialize speech listener
        speechListener = new SpeechListener(this, this);

        micButton.setOnClickListener(v -> {
            if (!micButton.isAnimating()) {
                micButton.playAnimation();
            }
            speechListener.startListening();
        });
    }


    private boolean isNotiaccessenable(){
        String enablelisteners = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        return enablelisteners !=null && enablelisteners.contains(getPackageName());
    }

    private void reqnotiaccess(){
        Intent intent=new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }


    @Override
    public void onSpeechRecognized(String query) {
        String textLower = query.toLowerCase(); // ✅ ADDED for simpler checks

        if (textLower.contains("call")) {
            String contactName = extractContactName(query);
            if (contactName != null) {
                callHelper.makePhoneCall(contactName);
            } else {
                respond("I couldn't identify the contact name.");
            }
        } else if (textLower.contains("weather")) {
            String city = extractCityFromQuery(query);
            weatherHelper.fetchWeather(city);
        } else if (textLower.contains("set alarm")) {
            String time = extractTimeFromQuery(query);
            Log.d("AlarmDebug", "Extracted time: " + time);
            if (time != null) {
                setAlarm(time);
            } else {
                respond("I couldn't understand the time. please say it again");
            }
        } else if (textLower.contains("open ") || textLower.contains("launch ") || textLower.contains("start ")) {
            String appName = extractAppName(query);
            if (!appName.isEmpty()) {
                appLaunch.openApp(appName);
            } else {
                respond("Sorry, I didn't understand the app name.");
            }

        } else if (textLower.contains("read notification") || textLower.contains("notification read")) { // ✅ ADDED
            texttoSpeech.speak("Reading notifications...");
            responseText.setText("Reading notifications...");
            NotificationRead.readnoti();
            return;

        } else if (textLower.contains("exit the app") || textLower.contains("close the app")
                || textLower.contains("exit") || textLower.contains("close")) { // ✅ ADDED
            texttoSpeech.speak("Closing the app");
            finishAffinity();
            return;

        } else if (textLower.contains("turn on flashlight") || textLower.contains("enable flashlight")
                || textLower.contains("on flash") || textLower.contains("flashlight on")) { // ✅ ADDED
            texttoSpeech.speak("Turning on flashlight");
            responseText.setText("Turning on flashlight");
            flashLight.turnonflash();
            return;

        } else if (textLower.contains("turn off flashlight") || textLower.contains("disable flashlight")
                || textLower.contains("off flash") || textLower.contains("flashlight off")) { // ✅ ADDED
            texttoSpeech.speak("Turning off flashlight");
            responseText.setText("Turning off flashlight");
            flashLight.turnoffflash();
            return;

        } else {
//            respond("I didn't understand.");
            responseText.setText("Let me think...");
            texttoSpeech.speak("I didn't understand. Let me find something helpful.");

            GeminiHelper.fetchFallbackResponse(query, new GeminiHelper.GeminiCallback() {
                @Override
                public void onResponse(String geminiReply) {
                    runOnUiThread(() -> {
                        responseText.setText(geminiReply);
                        texttoSpeech.speak(geminiReply);
                    });
                }
            });
        }
    }


    public void onSpeechRecognized1(String query) {
        if (query == null || query.isEmpty()) return;

        Log.d("SpeechRecognized", "Query: " + query);

        // ✅ Reuse the already initialized matcher
        String intent = nlpIntentMatcher.matchIntent(query);

        switch (intent) {
            case "call":
                handleCallIntent(query);
                break;
            case "weather":
                handleWeatherIntent(query);
                break;
            case "alarm":
                handleAlarmIntent(query);
                break;
            case "flashlight":
                handleFlashlightIntent(query);
                break;
            case "open_app":
                handleOpenAppIntent(query);
                break;
            case "read_notifications":
                handleReadNotificationsIntent();
                break;
            case "exit":
                finish();
                break;
            default:
                texttoSpeech.speak("Sorry, I didn't understand that.");
                break;
        }
    }



    private void handleCallIntent(String query) {
        String contactName = extractContactName(query);
        if (contactName != null) {
            callHelper.makePhoneCall(contactName);
        } else {
            respond("I couldn't identify the contact name.");
        }
    }

    private void handleWeatherIntent(String query) {
        String city = extractCityFromQuery(query);
        weatherHelper.fetchWeather(city);
    }

    private void handleAlarmIntent(String query) {
        String time = extractTimeFromQuery(query);
        if (time != null) {
            setAlarm(time);
        } else {
            respond("I couldn't understand the time.");
        }
    }

    private void handleFlashlightIntent(String query) {
        if (query.toLowerCase().contains("off")) {
            flashLight.turnoffflash();
            respond("Turning off flashlight");
        } else {
            flashLight.turnonflash();
            respond("Turning on flashlight");
        }
    }

    private void handleOpenAppIntent(String query) {
        String appName = extractAppName(query);
        if (!appName.isEmpty()) {
            appLaunch.openApp(appName);
        } else {
            respond("Sorry, I didn't understand the app name.");
        }
    }

    private void handleReadNotificationsIntent() {
        texttoSpeech.speak("Reading notifications...");
        responseText.setText("Reading notifications...");
        NotificationRead.readnoti();
    }



    @Override
    public void onSpeechError(String error) {
        respond(error);
    }

    @Override
    public void onSpeechEnd() {
        runOnUiThread(() -> {
            micButton.pauseAnimation();
            micButton.setFrame(0);
        });
    }

    private void respond(String message) {
        responseText.setText(message);
        texttoSpeech.speak(message);
    }

    private String extractAppName(String text) {
        if (text.contains("open ")) {
            return text.substring(text.indexOf("open ") + 5);
        } else if (text.contains("launch ")) {
            return text.substring(text.indexOf("launch ") + 7);
        } else if (text.contains("start ")) {
            return text.substring(text.indexOf("start ") + 6);
        }
        return "";
    }

    private String extractContactName(String query) {
        String[] words = query.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("call") && i + 1 < words.length) {
                return words[i + 1];
            }
        }
        return null;
    }

    private String extractCityFromQuery(String query) {
        for (String word : query.split(" ")) {
            if (Character.isUpperCase(word.charAt(0)) && word.length() > 2) {
                return word;
            }
        }
        return "delhi";
    }

    private String extractTimeFromQuery(String query) {
        query = query.toLowerCase();
        Pattern pattern = Pattern.compile("(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm)?");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            int hour = Integer.parseInt(matcher.group(1));
            int minute = (matcher.group(2) != null) ? Integer.parseInt(matcher.group(2)) : 0;
            String meridian = (matcher.group(3) != null) ? matcher.group(3) : "am";
            return hour + ":" + minute + " " + meridian;
        }
        return null;
    }

    private void setAlarm(String time) {
        String[] parts = time.split(" ");
        String[] timeParts = parts[0].split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = (timeParts.length > 1) ? Integer.parseInt(timeParts[1]) : 0;

        boolean isPM = parts.length > 1 && parts[1].equalsIgnoreCase("pm");

        if (isPM && hour < 12) hour += 12;
        if (!isPM && hour == 12) hour = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            respond("Alarm set for " + time);
        } else {
            respond("Failed to set alarm.");
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE
            }, CONTACTS_PERMISSION_REQUEST_CODE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onWeatherResponse(String weatherInfo) {
        respond(weatherInfo);
    }

    @Override
    public void onFailure(String errorMessage) {
        respond(errorMessage);
    }

    @Override
    protected void onDestroy() {
        if (texttoSpeech != null) {
            texttoSpeech.shutdown();
        }
        super.onDestroy();
    }
}
