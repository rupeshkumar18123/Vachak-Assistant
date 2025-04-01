//package com.example.voiceassistant_1;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//
//import java.util.List;
//
//    public class AppLaunch {
//    private final Context context;
//    private final TexttoSpeech texttoSpeech;
//
//    public AppLaunch(Context context, TexttoSpeech texttoSpeech){
//        this.context=context;
//        this.texttoSpeech= texttoSpeech;
//    }
//
//    public void openApp(String spokenApp){
//        PackageManager pm = context.getPackageManager();
//        List<ApplicationInfo> apps =pm.getInstalledApplications(0);
//        String matchedpackagename=null;
//        String spokenapplower=spokenApp.trim().toLowerCase();
//
//        for(ApplicationInfo applicationInfo:apps){
//            String appname= pm.getApplicationLabel(applicationInfo).toString().toLowerCase();
//            if(appname.equalsIgnoreCase(spokenapplower) || appname.contains(spokenapplower)){
//                matchedpackagename=applicationInfo.packageName;
//                break;
//            }
//        }
//        if(matchedpackagename!=null){
//            Intent intent = pm.getLaunchIntentForPackage(matchedpackagename);
//                if(intent!=null){
//                    context.startActivity(intent);
//                    texttoSpeech.speak("Opening "+ spokenApp);
//                }
//                else {
//                    texttoSpeech.speak("I cannot open "+ spokenApp);
//                }
//        }
//        else{
//                texttoSpeech.speak("I could not find "+spokenApp+" on your device.");
//        }
//    }
//}

//package com.example.voiceassistant_1;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AppLaunch {
//    private final Context context;
//    private final TexttoSpeech texttoSpeech;
//    private final Map<String, String> appMappings;
//
//    public AppLaunch(Context context, TexttoSpeech texttoSpeech) {
//        this.context = context;
//        this.texttoSpeech = texttoSpeech;
//
//        // 🔹 Map app names to package names (Xiaomi + General Apps)
//        appMappings = new HashMap<>();
//
//        // 🔹 System Apps
//        appMappings.put("calculator", "com.miui.calculator"); // Xiaomi Calculator
//        appMappings.put("camera", "com.android.camera"); // Default Camera
//        appMappings.put("clock", "com.android.deskclock"); // Default Clock
//        appMappings.put("contacts", "com.android.contacts"); // Contacts
//        appMappings.put("compass", "com.miui.compass"); // Xiaomi Compass
//        appMappings.put("notes", "com.miui.notes"); // Xiaomi Notes
//
//        // 🔹 Popular Apps
//        appMappings.put("snapchat", "com.snapchat.android");
//        appMappings.put("spotify", "com.spotify.music");
//        appMappings.put("telegram", "org.telegram.messenger");
//
//        // 🔹 Games
//        appMappings.put("clash of clans", "com.supercell.clashofclans");
//        appMappings.put("bgmi", "com.pubg.imobile"); // BGMI (India Version)
//    }
//
//    public void openApp(String spokenApp) {
//        PackageManager pm = context.getPackageManager();
//        String packageName = appMappings.get(spokenApp.toLowerCase());
//
//        if (packageName == null) {
//            // 🔹 Search for the app dynamically
//            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
//            for (ApplicationInfo app : apps) {
//                String appName = pm.getApplicationLabel(app).toString().toLowerCase();
//                if (appName.contains(spokenApp.toLowerCase())) {
//                    packageName = app.packageName;
//                    break;
//                }
//            }
//        }
//
//        if (packageName != null) {
//            Intent intent = pm.getLaunchIntentForPackage(packageName);
//            if (intent != null) {
//                context.startActivity(intent);
//                texttoSpeech.speak("Opening " + spokenApp);
//            } else {
//                texttoSpeech.speak("I cannot open " + spokenApp);
//            }
//        } else {
//            texttoSpeech.speak("I could not find " + spokenApp + " on your device.");
//        }
//    }
//}

package com.example.voiceassistant_1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLaunch {
    private final Context context;
    private final TexttoSpeech texttoSpeech;

    // Predefined package names for common apps
    private static final Map<String, String> APP_PACKAGE_MAP = new HashMap<>();

    static {
        APP_PACKAGE_MAP.put("instagram", "com.instagram.android");
        APP_PACKAGE_MAP.put("calculator", "com.android.calculator2"); // Default Android Calculator
        APP_PACKAGE_MAP.put("calculator (xiaomi)", "com.miui.calculator"); // Xiaomi
        APP_PACKAGE_MAP.put("clock", "com.android.deskclock");
        APP_PACKAGE_MAP.put("contacts", "com.android.contacts");
        APP_PACKAGE_MAP.put("compass", "com.miui.compass"); // Example for Huawei
        APP_PACKAGE_MAP.put("snapchat", "com.snapchat.android");
        APP_PACKAGE_MAP.put("spotify", "com.spotify.music");
        APP_PACKAGE_MAP.put("telegram", "org.telegram.messenger");
        APP_PACKAGE_MAP.put("notes", "com.miui.notes"); // Xiaomi Notes
        APP_PACKAGE_MAP.put("clash of clans", "com.supercell.clashofclans");
        APP_PACKAGE_MAP.put("bgmi", "com.pubg.imobile"); // Battlegrounds Mobile India
        APP_PACKAGE_MAP.put("camera", "com.android.camera"); // Default Android Camera
    }

    public AppLaunch(Context context, TexttoSpeech texttoSpeech) {
        this.context = context;
        this.texttoSpeech = texttoSpeech;
    }

    public void openApp(String spokenApp) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        String matchedPackageName = null;
        String spokenAppLower = spokenApp.trim().toLowerCase();

        // Step 1: Check predefined list
        if (APP_PACKAGE_MAP.containsKey(spokenAppLower)) {
            matchedPackageName = APP_PACKAGE_MAP.get(spokenAppLower);
        }

        // Step 2: If not found, search dynamically
        if (matchedPackageName == null) {
            for (ApplicationInfo appInfo : apps) {
                String appName = pm.getApplicationLabel(appInfo).toString().toLowerCase();
                if (appName.contains(spokenAppLower)) {
                    matchedPackageName = appInfo.packageName;
                    break;
                }
            }
        }

        // Step 3: Try to launch the app
        if (matchedPackageName != null) {
            Intent intent = pm.getLaunchIntentForPackage(matchedPackageName);
            if (intent != null) {
                context.startActivity(intent);
                texttoSpeech.speak("Opening " + spokenApp);
            } else {
                texttoSpeech.speak("I cannot open " + spokenApp);
            }
        } else {
            texttoSpeech.speak("I could not find " + spokenApp + " on your device.");
        }
    }
}

