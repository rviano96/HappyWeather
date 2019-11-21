package ar.iua.edu.viano.happyWeather.Persistence.Database.Users;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherRoomDatabase;

public class UserRepository {
    private UserDao userDao;
    private List<User> users;

    public UserRepository(Application application) {
        WeatherRoomDatabase database = WeatherRoomDatabase.getDatabase(application);
        userDao = database.userDao();
    }

    public List<User> getUserByName(String username) {
        try {
            users = new getUserAsyncTask(userDao).execute(username).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void insert(User user) {
        new insertUser(userDao).execute(user);
    }

    public void updateUser(User user){
        new updateUser(userDao).execute(user);
    }
    private static class insertUser extends AsyncTask<User, Void, Void> {

        private UserDao asyncTaskUserDao;

        insertUser(UserDao userDao) {
            asyncTaskUserDao = userDao;
        }

        @Override
        protected Void doInBackground(User... user) {
            asyncTaskUserDao.insert(user[0]);
            return null;
        }
    }

    private static class updateUser extends AsyncTask<User, Void, Void> {

        private UserDao asyncTaskUserDao;

        updateUser(UserDao userDao) {
            asyncTaskUserDao = userDao;
        }

        @Override
        protected Void doInBackground(User... user) {
            asyncTaskUserDao.updateUser(user[0]);
            return null;
        }
    }

    private static class getUserAsyncTask extends AsyncTask<String, Void, List<User>> {
        private UserDao asyncTaskUserDao;

        getUserAsyncTask(UserDao userDao) {
            asyncTaskUserDao = userDao;
        }

        @Override
        protected List<User> doInBackground(String... username) {
            return asyncTaskUserDao.getUserByUsername(username[0]);
        }
    }
}
