package com.example.voiceassistant_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

//public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Alarm is ringing!", Toast.LENGTH_LONG).show();
//
//        // Play a sound
//        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alram_sound);
//        mediaPlayer.start();
//    }
//}

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm triggered!"); // ✅ Debug log
        Toast.makeText(context, "Alarm is ringing!", Toast.LENGTH_LONG).show();

        // Play a sound
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alram_sound);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            Log.e("AlarmReceiver", "Failed to play alarm sound"); // ✅ Debug sound error
        }
    }//just a change
}


