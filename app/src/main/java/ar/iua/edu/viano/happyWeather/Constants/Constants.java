package ar.iua.edu.viano.happyWeather.Constants;

import android.os.Environment;

public final class Constants {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String USER_DATA = "USER_DATA";
    public static final String OPEN_BROWSER = "OPEN_BROWSER";
    public static final String UID= "b7599c710a89960a05499107667f4307";
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final  String CAMERA = ROOT_DIR + "/DCIM/camera";
    public static final String MAIN_FILE = "/DCIM/";
    public static final String IMAGE_FILE = "camera";
    public static final String IMAGE_DIRECTORY = MAIN_FILE + IMAGE_FILE;
    public static final double BASE_TEMP_CELCIUS = 273.15;
    public static final double BASE_TEMP_KELVINS = 459.67;
    public static final double BASE_KM_PER_HOURS = 3.6;
    public static final double BASE_MILES_PER_HOURS = 2.24;
    public static final int NOTIFICATION_REQUEST_CODE = 10;
    public static final double METER_TO_MILE = 0.000621371;
    public static final int UPDATE_WEATHER_REQUEST_CODE = 11;
}