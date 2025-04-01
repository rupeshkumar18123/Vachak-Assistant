package com.example.voiceassistant_1;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TexttoSpeech {
    private TextToSpeech textToSpeech;
    private String pendingtext=null;
    private boolean isready=false;

    public TexttoSpeech(Context context){
        textToSpeech = new TextToSpeech(context,status -> {
           if(status== TextToSpeech.SUCCESS){
               textToSpeech.setLanguage(Locale.ENGLISH);
               isready=true;

               if(pendingtext!=null){
                   speak(pendingtext);
                   pendingtext=null;
               }
           }
        });
    }

    public void speak(String text){
        if(isready){
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            pendingtext=text;
        }
    }

    public void shutdown(){
        if (textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


}
