package com.example.voiceassistant_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationRead extends NotificationListenerService {
    private static final String tag = "NotificationReader";
    private static TexttoSpeech texttoSpeech;
    private static final int max_noti = 5;
    private static final List<String> notificationlist = new ArrayList<>();

    public static void init(TexttoSpeech tts) {
        texttoSpeech = tts;
    }

    @SuppressLint("NewApi")
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        String packagename = sbn.getPackageName();
        String notificationtext = sbn.getNotification().extras.getString("android.text");

        if(notificationtext != null){
            String message = "New notification from " + packagename + " : " + notificationtext;
            Log.d(tag, message);

            if(notificationlist.size() >= max_noti){
                notificationlist.remove(0);
            }
            notificationlist.add(notificationtext);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d(tag, "Notification removed " + sbn.getPackageName());
    }

    public static void readnoti(){
        if(texttoSpeech == null) {
            Log.e(tag, "TexttoSpeech not initialized!");
            return;
        }

        if(notificationlist.isEmpty()){
            texttoSpeech.speak("You have no new notifications.");
        } else {
            for(String notification : notificationlist){
                texttoSpeech.speak(notification);
            }
            notificationlist.clear();
        }
    }
}
