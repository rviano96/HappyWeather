package ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ar.iua.edu.viano.happyWeather.Persistence.Data.DailyWeather;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherRoomDatabase;

public class DailyWeatherRepository {
    private DailyWeatherDao dailyWeatherDao;
    private List<DailyWeather> allHours;

    public DailyWeatherRepository(Application application) {
        WeatherRoomDatabase database = WeatherRoomDatabase.getDatabase(application);
        dailyWeatherDao = database.dailyWeatherDao();
    }

    public List<DailyWeather> getAllHours() {
        try {
            allHours = new getDaysAsyncTask(dailyWeatherDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allHours;
    }

    public void insert(DailyWeather weather) {
        new insterWeatherDialy(dailyWeatherDao).execute(weather);
    }

    private static class insterWeatherDialy extends AsyncTask<DailyWeather, Void, Void> {

        private DailyWeatherDao asyncTaskDailyWeatherDao;

        insterWeatherDialy(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected Void doInBackground(DailyWeather... weathers) {
            asyncTaskDailyWeatherDao.insert(weathers[0]);
            return null;
        }
    }

    private static class getDaysAsyncTask extends AsyncTask<Void, Void, List<DailyWeather>> {
        private DailyWeatherDao asyncTaskDailyWeatherDao;

        getDaysAsyncTask(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected List<DailyWeather> doInBackground(Void... voids) {
            return asyncTaskDailyWeatherDao.getAllDaysOrdered();
        }
    }
}
