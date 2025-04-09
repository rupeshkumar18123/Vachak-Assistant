//package com.example.voiceassistant_1;
//
//import java.util.*;
//
//public class IntentWordList {
//
//    public static Map<String, List<String>> getIntentMap() {
//        Map<String, List<String>> intentMap = new HashMap<>();
//
//        intentMap.put("CALL", Arrays.asList(
//                "call", "dial", "make a call", "phone", "ring", "call up"
//        ));
//
//        intentMap.put("WEATHER", Arrays.asList(
//                "weather", "forecast", "temperature", "climate", "how's the weather", "rainy"
//        ));
//
//        intentMap.put("ALARM", Arrays.asList(
//                "set alarm", "alarm", "wake me", "remind me", "alarm for", "reminder"
//        ));
//
//        intentMap.put("APP_LAUNCH", Arrays.asList(
//                "open", "launch", "start", "run", "go to", "access"
//        ));
//
//        intentMap.put("NOTIFICATIONS", Arrays.asList(
//                "read notifications", "notification read", "show notifications", "any notifications"
//        ));
//
//        intentMap.put("FLASHLIGHT_ON", Arrays.asList(
//                "turn on flashlight", "enable flashlight", "on flash", "light on", "torch on", "flashlight on"
//        ));
//
//        intentMap.put("FLASHLIGHT_OFF", Arrays.asList(
//                "turn off flashlight", "disable flashlight", "off flash", "light off", "torch off", "flashlight off"
//        ));
//
//        intentMap.put("EXIT", Arrays.asList(
//                "exit", "close the app", "exit the app", "quit", "terminate", "shut down"
//        ));
//
//        return intentMap;
//    }
//}


package com.example.voiceassistant_1;

import java.util.*;

public class IntentWordList {
    public static final Map<String, List<String>> intentWords;

    static {
        intentWords = new HashMap<>();

        intentWords.put("CALL", Arrays.asList("call", "dial", "make a call", "phone", "ring", "call up"));
        intentWords.put("WEATHER", Arrays.asList("weather", "forecast", "temperature", "climate", "how's the weather", "rainy"));
        intentWords.put("ALARM", Arrays.asList("set alarm", "alarm", "wake me", "remind me", "alarm for", "reminder"));
        intentWords.put("APP_LAUNCH", Arrays.asList("open", "launch", "start", "run", "go to", "access"));
        intentWords.put("NOTIFICATIONS", Arrays.asList("read notifications", "notification read", "show notifications", "any notifications"));
        intentWords.put("FLASHLIGHT_ON", Arrays.asList("turn on flashlight", "enable flashlight", "on flash", "light on", "torch on", "flashlight on"));
        intentWords.put("FLASHLIGHT_OFF", Arrays.asList("turn off flashlight", "disable flashlight", "off flash", "light off", "torch off", "flashlight off"));
        intentWords.put("EXIT", Arrays.asList("exit", "close the app", "exit the app", "quit", "terminate", "shut down"));
    }
}
