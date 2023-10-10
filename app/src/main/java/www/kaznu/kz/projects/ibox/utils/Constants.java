package www.kaznu.kz.projects.ibox.utils;

import www.kaznu.kz.projects.ibox.activities.LoginActivity;

public class Constants {

    public static final String SERVER_ADDRESS = "a0868210.xsph.ru";
    public static String GET_URL = "http://" + SERVER_ADDRESS + "AndroidApi/volley/device_data.php";
    public static final String POST_URL = "http://" + SERVER_ADDRESS + "AndroidApi/volley/insert.php";
    public static final String TAG = "bleService";

    public static final String FIND_DEVICE_ALARM_ON = "find.device.alarm.on";
    public static final String CANCEL_DEVICE_ALARM = "find.device.cancel.alarm";

    public static final String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_COARSE_LOCATION"
    };

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_CHAR_READED = "com.example.bluetooth.le.ACTION_CHAR_READED";
    public final static String BATTERY_LEVEL_AVAILABLE = "com.example.bluetooth.le.BATTERY_LEVEL_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String EXTRA_STRING_DATA = "com.example.bluetooth.le.EXTRA_STRING_DATA";
    public final static String EXTRA_DATA_LENGTH = "com.example.bluetooth.le.EXTRA_DATA_LENGTH";
    public final static String ACTION_GATT_RSSI = "com.example.bluetooth.le.ACTION_GATT_RSSI";
    public final static String EXTRA_DATA_RSSI = "com.example.bluetooth.le.ACTION_GATT_RSSI";

    public static final String ID = "id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String AQI = "AQI";
    public static final String PM01 = "PM1";
    public static final String PM10 = "PM10";
    public static final String PM25 = "PM25";
    public static final String HUMIDITY = "humidity";
    public static final String PRESSURE = "pressure";
    public static final String TEMPERATURE = "temperature";
}

