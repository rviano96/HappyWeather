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
    private static final String UPDATE_RATIO = "update_ratio";

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
        return sharedPreferences.getString(LAST_UPDATE, "default value");
    }

    public void setUnits(String units) {
        sharedPreferences.edit().putString(UNITS, units).apply();
    }

    public String getUnits() {
        return sharedPreferences.getString(UNITS, "default value");
    }

    public void setUpdateRatio(int updateRatio) {
        sharedPreferences.edit().putInt(UPDATE_RATIO, updateRatio).apply();
    }

    public int getUpdateRatio() {
        return sharedPreferences.getInt(UPDATE_RATIO, 1);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "default value");
    }

    public void setUserEmail(String userEmail) {
        sharedPreferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "default value");
    }

    public void setUserPicture(String userPicture) {
        sharedPreferences.edit().putString(USER_PICTURE, userPicture).apply();
    }

    public String getUserPicture() {
        return sharedPreferences.getString(USER_PICTURE, "default value");
    }

    public void setUserIsLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(USER_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean getUserIsLoggedIn() {
        return sharedPreferences.getBoolean(USER_IS_LOGGED_IN, false);
    }

}
