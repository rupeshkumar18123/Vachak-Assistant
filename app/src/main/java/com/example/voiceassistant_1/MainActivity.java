package com.example.voiceassistant_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnSpeechRecognizedListener{
    private SpeechListener speechListener;
    private TexttoSpeech texttoSpeech;
    private AppLaunch appLaunch;
    private static final int req_record_audio=1;
    private LottieAnimationView micbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},req_record_audio);
        }

        VideoView videoView = findViewById(R.id.videoView);
        micbtn = findViewById(R.id.micButton);

        Uri videouri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sample);
        videoView.setVideoURI(videouri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
        });

        texttoSpeech = new TexttoSpeech(this);
        appLaunch= new AppLaunch(this,texttoSpeech);

        texttoSpeech.speak("Hello! What can i help you with?");
        speechListener = new SpeechListener(this,this);

        micbtn.setOnClickListener(v -> {

            if(!micbtn.isAnimating()){
                micbtn.playAnimation();
            }

            speechListener.startListening();

        });

    }

    @Override
    public void onSpeechRecognized(String text) {
        String textLower = text.toLowerCase();
        String appName = "";

        if (textLower.contains("open ")) {
            appName = text.substring(textLower.indexOf("open ") + 5);
        } else if (textLower.contains("launch ")) {
            appName = text.substring(textLower.indexOf("launch ") + 7);
        } else if (textLower.contains("start ")) {
            appName = text.substring(textLower.indexOf("start ") + 6);
        }

        if (!appName.isEmpty()) {
            appLaunch.openApp(appName);
        } else {
            texttoSpeech.speak("Sorry, I didn't understand.");
        }
//        if(text.toLowerCase().startsWith("open ")){
//            String appName = text.substring(5);
//            appLaunch.openApp(appName);
//        }

    }

    @Override
    public void onSpeechError(String error) {
        texttoSpeech.speak(error);

    }

    @Override
    public void onSpeechEnd() {
        runOnUiThread(()->{
            micbtn.pauseAnimation();
            micbtn.setFrame(0);
        });
    }

    @Override
    protected void onDestroy() {
        if(texttoSpeech!=null){
            texttoSpeech.shutdown();
        }
        super.onDestroy();
    }
}