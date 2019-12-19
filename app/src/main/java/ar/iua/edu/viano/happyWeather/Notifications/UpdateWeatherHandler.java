package ar.iua.edu.viano.happyWeather.Notifications;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Activities.HomeActivity;
import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.GPS;
import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.Services.WeatherService;

public class UpdateWeatherHandler extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_ID = "my_channel";
    private PreferencesUtils preferencesUtils;
    private ConvertUnits convertUnits = new ConvertUnits();
    private WeatherForecastRepository weatherForecastRepository;
    private DailyWeatherRepository dailyWeatherRepository;
    private Context context;
    private PendingIntent pendingIntent;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private double lat;
    private double lon;
    GPS g;
    Location l;
    void saveData(List<WeatherFromApi> data) {
        weatherForecastRepository = new WeatherForecastRepository((Application) context.getApplicationContext());
        dailyWeatherRepository = new DailyWeatherRepository((Application) context.getApplicationContext());
        WeatherFromApi dto = data.get(0);
        String name = dto.getName();
        WeatherDetails weatherDetails;
        Long day = dto.getDt();
        List<Weather> weather = new ArrayList<>();
        List<WeatherForecast> weatherForecast = new ArrayList<>();
        //simular 1 dia 24hs
        for (int i = 0; i < 8; i++) {
            dto = data.get(i);
            weatherDetails = new WeatherDetails(String.valueOf(dto.getMain().getHumidity()),
                    String.valueOf(dto.getMain().getPressure()), dto.getWind().getSpeed(),
                    String.valueOf(dto.getVisibility()));
            weather.add(new Weather(dto.getMain().getTempMax(), dto.getMain().getTempMin(), dto.getMain().getTemp(),
                    name, new Date(dto.getDt() * 1000), data.get(i).getListWeather().get(0).getId(), weatherDetails));
        }
        int position = 0;
        // me fijo cuando el campo dateText es diferente,
        //entonces tomo la posicion anterior y a partir de esa, cada nuevo dia se va a encontrar cada 8 posiciones
        for (int i = 2; i <= data.size(); i++) {
            if (!data.get(i - 1).getDateTxt().split(" ")[0].equals(data.get(i).getDateTxt().split(" ")[0])) {
                position = i - 1;
                break;
            }
        }
        Calendar calendar = Calendar.getInstance();
        for (int i = position; i < data.size(); i += 8) {
            //29.0+i,15-i,"Cordoba", setDOW(calendar.get(Calendar.DAY_OF_WEEK)), 1)
            calendar.setTime(new Date(data.get(i).getDt() * 1000));
            weatherForecast.add(new WeatherForecast(data.get(i).getMain().getTempMax(), data.get(i).getMain().getTempMin(),
                    name, String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)), data.get(i).getListWeather().get(0).getId()));
        }
        dailyWeatherRepository.delete();
        weatherForecastRepository.deleteWeatherForecast();
        for (Weather w : weather) {
            dailyWeatherRepository.insert(w);
        }
        for (WeatherForecast wf : weatherForecast) {
            weatherForecastRepository.insert(wf);
        }
        calendar.setTime(new Date());

        preferencesUtils.setLastUpdate(formatLastUpdate(new Date().getHours(), new Date().getMinutes()));
        preferencesUtils.setActualTemp(convertUnits.formatTemperature(weather.get(0).getActualTemp(), preferencesUtils.getUnits()));
        preferencesUtils.setMaxTemp(convertUnits.formatTemperature(weather.get(0).getMaximum(), preferencesUtils.getUnits()));
        Log.d("updated", preferencesUtils.getLastUpdate());
    }

    private String formatLastUpdate(int hourOfDay, int minute){
        String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
        String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
        String AM_PM;
        if (hourOfDay < 12) {
            AM_PM = "a.m.";
        } else {
            AM_PM = "p.m.";
        }
        return (horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM) ;
    }
    // se llama cuando la clase recibe el trigger
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        preferencesUtils = new PreferencesUtils(context);
        // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, HomeActivity.class);// clase que se ejecuta cuando se hace click en la notificacion
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.pendingIntent = PendingIntent.getActivity(context, Constants.UPDATE_WEATHER_REQUEST_CODE, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //sendNotification(pendingIntent);
        Context cont[] = {context};
        new NotificationAsyncTask().execute(cont);
    }

    private class NotificationAsyncTask extends AsyncTask<Context, Void, Void> {
        PreferencesUtils preferencesUtils;

        @Override
        protected Void doInBackground(Context... contexts) {
            try {
                Context context = contexts[0];
                WeatherService weatherService = new WeatherService(context);
                preferencesUtils = new PreferencesUtils(context);
                Weather weather;
                WeatherFromApi dto;
                List<WeatherFromApi> dtoList = new ArrayList<>();
                //strings 0 = latittud, strings 1 = longitud, strings 2 = units
                g = new GPS(context);
                l = g.getLocation();
                if (l != null) {
                    lat = l.getLatitude();
                    lon = l.getLongitude();
                    Log.d("coord", "LAT: " + lat + "LON: " + lon);
                }else{
                    lat = -31.41;
                    lon = -64.18;
                }
                List<WeatherFromApi> list = new ArrayList<>();
                //strings 0 = latittud, strings 1 = longitud, strings 2 = units
                String[] data = {String.valueOf(lat), String.valueOf(lon), "metric"};
                String [] coords = {data[0], data[1]};
                String strings[] = {"-31.41", "-64.18", "metric"};
                dto = weatherService.weatherFromApi(coords, strings[2]);
                dtoList.add(dto);
                dtoList.addAll(weatherService.forecastWeather(coords, strings[2]).getWeatherFromApiList());

                saveData(dtoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
