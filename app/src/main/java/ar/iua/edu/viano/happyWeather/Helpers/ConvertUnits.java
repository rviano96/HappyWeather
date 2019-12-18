package ar.iua.edu.viano.happyWeather.Helpers;

import android.util.Log;

import ar.iua.edu.viano.happyWeather.Constants.Constants;

public class ConvertUnits {


    public ConvertUnits() {
    }

    public String formatTemperature(double temp, boolean unit) {
        double tmp;
        String formated = "";
        if (unit) {
            tmp = temp - Constants.BASE_TEMP_CELCIUS;
            formated = String.format("%.0f °C", tmp);
        } else {
            tmp = (temp * 9 / 5) - Constants.BASE_TEMP_KELVINS;
            formated = String.format("%.0f °F", tmp);
        }
        Log.d("temperatura formateada", formated);
        return formated;
    }

    public String formatWind(String data, boolean unit) {
        double tmp;
        String formated = "";
        double wind = Double.parseDouble(data);
        if (unit) {
            tmp = wind * Constants.BASE_KM_PER_HOURS;
            formated = String.format("%.0f KM/H", tmp);
        } else {
            tmp = wind * Constants.BASE_MILES_PER_HOURS;
            formated = String.format("%.0f Mi/H", tmp);
        }
        Log.d("velocidad formateada", formated);
        return formated;
    }

    public String formatVisibility(String data, boolean unit) {
        double tmp;
        String formated = "";
        double visibility = Double.parseDouble(data);
        if (unit) {
            tmp = visibility/1000;
            formated = String.format("%.0f KM", tmp);
        } else {
            tmp = visibility * Constants.METER_TO_MILE;
            formated = String.format("%.0f Mi", tmp);
        }
        Log.d("visibilidad formateada", formated);
        return formated;
    }
}
