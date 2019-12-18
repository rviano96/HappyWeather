package ar.iua.edu.viano.happyWeather.Helpers;

import android.content.Context;

import ar.iua.edu.viano.happyWeather.R;

public class SetWeatherStatus {
    private Context context;

    public SetWeatherStatus(Context current) {
        this.context = current;
    }

    public String setStatus(String status) {
        char temp = status.charAt(0);

        switch (temp) {
            case '2':
                return context.getResources().getString(R.string.thunderstorm);
            case '3':
                return context.getResources().getString(R.string.drizzle);
            case '5':
                return context.getResources().getString(R.string.rainny);
            case '6':
                return context.getResources().getString(R.string.snowy);
            case '7':
                return context.getResources().getString(R.string.cloudy);
            default:
                String tmp = status.substring(0, 2);
                switch (tmp) {
                    case "800":
                        return context.getResources().getString(R.string.sunny);
                    case "80":
                        return context.getResources().getString(R.string.cloudy);
                    default:
                        return context.getResources().getString(R.string.sunny);

                }
        }
    }
}