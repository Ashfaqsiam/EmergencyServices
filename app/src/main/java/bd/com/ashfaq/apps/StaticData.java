package bd.com.ashfaq.apps;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaticData {
    public static String[] bangladeshDistricts = {"All", "Dhaka", "Chattogram", "Rajshahi", "Khulna", "Barishal", "Sylhet", "Rangpur", "Mymensingh"};


    public static ArrayList<Map<String, String>> getMyAppServices() {
        ArrayList<Map<String, String>> serviceList = new ArrayList<>();
        // Add icons and titles
        serviceList.add(addService("POLICE","icon_police", "Police"));
        serviceList.add(addService("AMBULANCE", "icon_ambulance", "Ambulance"));
        serviceList.add(addService("DOCTORS", "icon_doctor", "Doctor"));
        serviceList.add(addService("HOSPITAL", "icon_hospital", "Hospital"));
        serviceList.add(addService("FIRE_SERVICE", "icon_fire", "Fire Service"));
        serviceList.add(addService("RAB", "icon_rab", "RAB"));
        serviceList.add(addService("0", "icon_national_emergency", "Call 999"));
        return serviceList;
    }

    // Method to add service data to the list
    private static Map<String, String> addService(@NonNull String idX, String iconName, String title) {
        Map<String, String> service = new HashMap<>();
        service.put("id", String.valueOf(idX));
        service.put("icon", iconName);
        service.put("title", title);
        return service;
    }
}
