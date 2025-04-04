package com.example.voiceassistant_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
    private static final int req_camera=100;
    private LottieAnimationView micbtn;
    private FlashLight flashLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},req_record_audio);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},req_camera);
        }

        if(!isNotiaccessenable()){
            reqnotiaccess();
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
        speechListener = new SpeechListener(this,this);
        flashLight =new FlashLight(this,texttoSpeech);

        texttoSpeech.speak("Hello! What can i help you with?");

        micbtn.setOnClickListener(v -> {

            if(!micbtn.isAnimating()){
                micbtn.playAnimation();
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
    public void onSpeechRecognized(String text) {
        String textLower = text.toLowerCase();
        String appName = "";

        // for opening the apps
        if (textLower.contains("open ")) {
            appName = text.substring(textLower.indexOf("open ") + 5);
        } else if (textLower.contains("launch ")) {
            appName = text.substring(textLower.indexOf("launch ") + 7);
        } else if (textLower.contains("start ")) {
            appName = text.substring(textLower.indexOf("start ") + 6);
        }

        if (!appName.isEmpty()) {
            appLaunch.openApp(appName);
            return;
        }

        // for flashlight
        if(textLower.contains("turn on flashlight")|| textLower.contains("enable flashlight")
                || textLower.contains("on flash") || textLower.contains("flashlight on")){
            flashLight.turnonflash();
            return;
        } else if (textLower.contains("turn off flashlight") || textLower.contains("disable flashlight")
                || textLower.contains("off flash") || textLower.contains("flashlight off")) {
            flashLight.turnoffflash();
            return;
        }

        // for reading notification
        if (textLower.contains("read notifications") || textLower.contains("notification read")){
            NotificationRead.readnoti();
            return;
        }

        texttoSpeech.speak("Sorry I didn't understand.");
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