//package com.example.voiceassistant_1;
//
//import java.util.*;
//import java.util.regex.*;
//
//public class NLPIntentMatcher {
//    private final Map<String, List<String>> intentMap;
//
//    public NLPIntentMatcher() {
//        this.intentMap = IntentWordList.getIntentMap();
//    }
//
//    public String matchIntent(String query) {
//        query = query.toLowerCase().trim().replaceAll("[^a-zA-Z0-9\\s]", "");
//
//        for (Map.Entry<String, List<String>> entry : intentMap.entrySet()) {
//            for (String keyword : entry.getValue()) {
//                String regex = "\\b" + Pattern.quote(keyword.toLowerCase()) + "\\b";
//                if (Pattern.compile(regex).matcher(query).find()) {
//                    return entry.getKey();
//                }
//            }
//        }
//        return "UNKNOWN";
//    }
//}
package com.example.voiceassistant_1;

import java.util.List;
import java.util.Map;

public class NLPIntentMatcher {
    private final Map<String, List<String>> intentMap;

    public NLPIntentMatcher(Map<String, List<String>> externalIntentMap) {
        this.intentMap = externalIntentMap;
    }

    public String matchIntent(String query) {
        String lowerQuery = query.toLowerCase();
        for (Map.Entry<String, List<String>> entry : intentMap.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowerQuery.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }
        return "UNKNOWN";
    }
}
