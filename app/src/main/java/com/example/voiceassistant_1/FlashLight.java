package com.example.voiceassistant_1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FlashLight {
    private CameraManager cameraManager;
    private String cameraId;
    private boolean Flashon=false;
    private Context context;
    private TexttoSpeech texttoSpeech;
    private static final int Cam_req_code=2;

    public FlashLight(Context context, TexttoSpeech texttoSpeech){
        this.context=context;
        this.texttoSpeech=texttoSpeech;
        cameraManager =(CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId=cameraManager.getCameraIdList()[0];
        }
        catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    public boolean checkcameraperms(){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
    }

    public void reqcameraperms(){
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},Cam_req_code);
    }

    public void turnonflash(){
        if(checkcameraperms()){
            try{
                cameraManager.setTorchMode(cameraId,true);
                Flashon=true;
                texttoSpeech.speak("FlashLight turned on");
            }
            catch (CameraAccessException e){
                e.printStackTrace();
                texttoSpeech.speak("Failed to turn on flash");
            }
        }
        else{
            reqcameraperms();
        }
    }

    public void turnoffflash(){
        if(checkcameraperms()){
            try{
                cameraManager.setTorchMode(cameraId,false);
                Flashon=false;
                texttoSpeech.speak("FlashLight turned off");
            }
            catch (CameraAccessException e){
                e.printStackTrace();
                texttoSpeech.speak("Failed to turn off flash");
            }
        }
        else{
            reqcameraperms();
        }
    }



}