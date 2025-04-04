package com.example.voiceassistant_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationRead extends NotificationListenerService {
    private static final String tag ="NotificationReader";
    private  static TexttoSpeech texttoSpeech;
    private static final int max_noti=5;
    private static final List<String> notificationlist = new ArrayList<>();

    @Override
    public void onCreate(){
        super.onCreate();
        texttoSpeech=new TexttoSpeech(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        String packagename=sbn.getPackageName();
        String notificationtext = sbn.getNotification().extras.getString("android.text");

        if(notificationtext!=null){
            String message = "New notification from "+packagename + " : " + notificationtext;
            Log.d(tag,message);

            if(notificationlist.size()>=max_noti){
                notificationlist.remove(0);
            }
            notificationlist.add(notificationtext);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d(tag,"Notification removed"+sbn.getPackageName());
    }

    public static void readnoti(){
        if(notificationlist.isEmpty()){
            texttoSpeech.speak("You have no new notifications.");
        }
        else{
            for(String notification : notificationlist){
                texttoSpeech.speak(notification);
            }
            notificationlist.clear();
        }
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags ,int startid){
//        if(intent!=null && "com.example.voiceassistant_1.READ_NOTIFICATIONS".equals(intent.getAction())){
//            readnoti();
//        }
//        return START_STICKY;
//    }

}
