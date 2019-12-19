package ar.iua.edu.viano.happyWeather.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PICTURE = "user_picture";
    private static final String USER_IS_LOGGED_IN = "user_is_logged_in";
    private static final String PREFS_NAME = "ar.iua.edu.viano.happyWeather";
    private static final String LAST_UPDATE = "last_update";
    private static final String UNITS = "units";
    private static final String NOTIFICATIONS = "notifications";
    private static final String ALARM_TIME = "alarm_time";
    private static final String ACTUAL = "actualTemp";
    private static final String MAX = "maxTemp";
    private static final String UPDATE_RATIO = "update_ratio";
    private static final String USER_PICTURE_LOCALE ="user_picture_locale";
    public PreferencesUtils(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void setUserName(String userName) {
        sharedPreferences.edit().putString(USER_NAME, userName).apply();

    }

    public void setLastUpdate(String lastUpdate) {
        sharedPreferences.edit().putString(LAST_UPDATE, lastUpdate).apply();
    }

    public String getLastUpdate() {
        return sharedPreferences.getString(LAST_UPDATE, "");
    }

    public void setAlarmTime(String alarmTime) {
        sharedPreferences.edit().putString(ALARM_TIME, alarmTime).apply();
    }

    public String getAlarmTime() {
        return sharedPreferences.getString(ALARM_TIME, "");
    }

    public void setNotifications(boolean notifications) {
        sharedPreferences.edit().putBoolean(NOTIFICATIONS, notifications).apply();
    }

    public boolean getNotifications() {
        return sharedPreferences.getBoolean(NOTIFICATIONS, false);
    }

    public void setActualTemp(String actualTemp) {
        sharedPreferences.edit().putString(ACTUAL, actualTemp).apply();
    }

    public String getActualTemp() {
        return sharedPreferences.getString(ACTUAL, "null");
    }

    public void setMaxTemp(String maxTemp) {
        sharedPreferences.edit().putString(MAX, maxTemp).apply();
    }

    public String getMaxTemp() {
        return sharedPreferences.getString(MAX, "null");
    }

    public void setUnits(boolean units) {
        sharedPreferences.edit().putBoolean(UNITS, units).apply();
    }

    public boolean getUnits() {
        return sharedPreferences.getBoolean(UNITS, false);
    }

    public void setUpdateRatio(int updateRatio) {
        sharedPreferences.edit().putInt(UPDATE_RATIO, updateRatio).apply();
    }

    public int getUpdateRatio() {
        return sharedPreferences.getInt(UPDATE_RATIO, 1);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        sharedPreferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public void setUserPicture(String userPicture) {
        sharedPreferences.edit().putString(USER_PICTURE, userPicture).apply();
    }

    public String getUserPicture() {
        return sharedPreferences.getString(USER_PICTURE, "");
    }

    public void setUserPictureLocale(String userPicture) {
        sharedPreferences.edit().putString(USER_PICTURE_LOCALE, userPicture).apply();
    }

    public String getUserPictureLocale() {
        return sharedPreferences.getString(USER_PICTURE_LOCALE, "");
    }
    public void setUserIsLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(USER_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean getUserIsLoggedIn() {
        return sharedPreferences.getBoolean(USER_IS_LOGGED_IN, false);
    }

    public void clearData(){
        setUserIsLoggedIn(false);
        setUserEmail("");
        setUserName("");
        setUserPicture("");
        setNotifications(false);
        setAlarmTime("");
        setMaxTemp("");
        setLastUpdate("");
        setUnits(false);
        setUpdateRatio(1);
        setUserPictureLocale("");
    }
}
