package ar.iua.edu.viano.happyWeather.Persistence.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ar.iua.edu.viano.happyWeather.Persistence.Data.DailyWeather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherDao;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastDao;

@Database(entities = {DailyWeather.class, WeatherForecast.class }, version = 1)
public abstract class WeatherRoomDatabase extends RoomDatabase {
    public abstract DailyWeatherDao dailyWeatherDao();
    public abstract WeatherForecastDao weatherForecastDao();

    private static volatile WeatherRoomDatabase INSTANCE;
    public static WeatherRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherRoomDatabase.class, "WeatherRoomDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}