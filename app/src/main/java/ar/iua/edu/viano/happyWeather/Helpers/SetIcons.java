package ar.iua.edu.viano.happyWeather.Helpers;

import android.content.Context;

import ar.iua.edu.viano.happyWeather.R;

public class SetIcons {
    private Context context;

    public SetIcons(Context current) {
        this.context = current;
    }

    public int findIcon(String status) {
        char temp = status.charAt(0);
        switch (temp) {
            case '2':
                return R.drawable.ic_iconfinder_thunderstorm;
            case '3':
                return (R.drawable.ic_iconfinder_rainny);
            case '5':
                return (R.drawable.ic_iconfinder_rainny);
            case '6':
                return (R.drawable.ic_iconfinder_snowflake_1651934);
            case '7':
                return (R.drawable.ic_iconfinder_cloudy);
            default:
                String tmp = status.substring(0, 2);
                switch (tmp) {
                    case "800":
                        return (R.drawable.ic_iconfinder_weather_01_1530392);
                    case "80":
                        return (R.drawable.ic_iconfinder_cloudy);
                    default:
                        return (R.drawable.ic_iconfinder_weather_01_1530392);
                }
        }
    }
}
