package ar.iua.edu.viano.happyWeather.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Activities.HomeActivity;
import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.Services.WeatherService;

public class NotificationHandler extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_ID = "my_channel";
    private PreferencesUtils preferencesUtils;
    private ConvertUnits convertUnits = new ConvertUnits();
    private Context context;
    private PendingIntent pendingIntent;
    void sendNotification() {
        preferencesUtils = new PreferencesUtils(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_happy_weather)
                .setContentTitle(context.getString(R.string.app_name))
                //String.format(getResources().getString(R.string.humidity), w.getHumidity())
                .setContentText(String.format(context.getString(R.string.notification_message),preferencesUtils.getUserName(), preferencesUtils.getActualTemp(),
                        preferencesUtils.getMaxTemp()))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Crear el Notification Channel para versiones de android posteriores a API 26.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = "Notification";
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(description);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Log.d("notification", String.format(context.getString(R.string.notification_message),preferencesUtils.getUserName(), preferencesUtils.getActualTemp(),
                preferencesUtils.getMaxTemp()));
        notificationManager.notify(Constants.NOTIFICATION_REQUEST_CODE, builder.build());

    }

    // se llama cuando la clase recibe el trigger
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, HomeActivity.class);// clase que se ejecuta cuando se hace click en la notificacion
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.pendingIntent = PendingIntent.getActivity(context, Constants.NOTIFICATION_REQUEST_CODE, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //sendNotification(pendingIntent);
        Context cont [] = {context};
        new NotificationAsyncTask().execute(cont);
    }
    private class NotificationAsyncTask extends AsyncTask<Context,Void,Void>{
        PreferencesUtils preferencesUtils;
        @Override
        protected Void doInBackground(Context... contexts) {
            try{
                Context context = contexts[0];
                WeatherService weatherService = new WeatherService(context);
                preferencesUtils = new PreferencesUtils(context);
                Weather weather;
                WeatherFromApi dto;
                List<WeatherFromApi> dtoList = new ArrayList<>();
                //strings 0 = latittud, strings 1 = longitud, strings 2 = units
                String strings [] = {"-31.41", "-64.18", "metric"};
                String[] coords = {strings[0], strings[1]};
                dto = weatherService.weatherFromApi(coords, strings[2]);
                dtoList.add(dto);
                preferencesUtils.setMaxTemp(convertUnits.formatTemperature(dtoList.get(0).getMain().getTempMax(), preferencesUtils.getUnits()));
                preferencesUtils.setActualTemp(convertUnits.formatTemperature(dtoList.get(0).getMain().getTemp(), preferencesUtils.getUnits()));
                sendNotification();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
