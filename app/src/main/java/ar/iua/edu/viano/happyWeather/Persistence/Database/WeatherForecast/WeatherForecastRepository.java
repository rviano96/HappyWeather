package ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherRoomDatabase;

public class WeatherForecastRepository {
    private WeatherForecastDao weatherDao;
    private List<WeatherForecast> allDays;

    public WeatherForecastRepository(Application application) {
        WeatherRoomDatabase database = WeatherRoomDatabase.getDatabase(application);
        weatherDao = database.weatherForecastDao();
    }

    public List<WeatherForecast> getAllDays() {
        try {
            allDays = new getDaysAsyncTask(weatherDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allDays;
    }

    public void insert(WeatherForecast weather) {
        new insterWeather(weatherDao).execute(weather);
    }

    private static class insterWeather extends AsyncTask<WeatherForecast, Void, Void> {

        private WeatherForecastDao asyncTaskWeatherDao;

        insterWeather(WeatherForecastDao weatherDao) {
            asyncTaskWeatherDao = weatherDao;
        }

        @Override
        protected Void doInBackground(WeatherForecast... weathers) {
            asyncTaskWeatherDao.insert(weathers[0]);
            return null;
        }
    }

    private static class getDaysAsyncTask extends AsyncTask<Void, Void, List<WeatherForecast>> {
        private WeatherForecastDao asyncTaskWeatherDao;

        getDaysAsyncTask(WeatherForecastDao weatherForecastDao) {
            asyncTaskWeatherDao = weatherForecastDao;
        }

        @Override
        protected List<WeatherForecast> doInBackground(Void... voids) {
            return asyncTaskWeatherDao.getAllDaysOrdered();
        }
    }
}
