package ar.iua.edu.viano.happyWeather.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.GPS;
import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;
import ar.iua.edu.viano.happyWeather.Notifications.NotificationHandler;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.Users.UserRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.Services.WeatherService;
import ar.iua.edu.viano.happyWeather.UI.fragments.MapFragment;
import ar.iua.edu.viano.happyWeather.UI.fragments.SettingsFragment;
import ar.iua.edu.viano.happyWeather.UI.fragments.WeatherFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.EditButtonListener,
        WeatherFragment.ReloadButtonListener {

    private User usuario;
    private DrawerLayout drawer;
    private String actualFragment = null;
    private PreferencesUtils preferencesUtils;
    //referencia al fragment que vamos a abrir
    Fragment selectedFragment = null;
    NavigationView navigationView = null;
    Bundle bundle = null;
    Bitmap photo = null;
    static final int REQUEST_TAKE_PHOTO = 0, REQUEST_SELECT_PICTURE = 1, REQUEST_CAMERA = 2, NOTIFICATION_REQUEST_CODE = Constants.NOTIFICATION_REQUEST_CODE;
    String currentPhotoPath;
    private UserRepository userRepository;
    private double lat;
    private double lon;
    GPS g;
    Location l;
    private WeatherForecastRepository weatherForecastRepository;
    private DailyWeatherRepository dailyWeatherRepository;
    private ConvertUnits convertUnits = new ConvertUnits();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesUtils = new PreferencesUtils(this);
        weatherForecastRepository = new WeatherForecastRepository(getApplication());
        dailyWeatherRepository = new DailyWeatherRepository(getApplication());
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository(getApplication());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PackageManager.GET_PERMISSIONS);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PackageManager.GET_PERMISSIONS);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PackageManager.GET_PERMISSIONS);

        }
        /*ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.INTERNET}, 1234);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 12345);*/
        setContentView(R.layout.activity_home);
        savedInstanceState = getIntent().getExtras();
        initUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drow_open, R.string.navigation_drow_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (actualFragment == null) {
            actualFragment = "MapFragment";
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment(), actualFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public void onBackPressed() {
        // si apretamos el boton de back y el drawer esta abierto, lo cerramos
        //si esta cerrado y esta en el fragment map llamamos al metodo super,
        //sino vamos al fragment map.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (actualFragment.equals("MapFragment")) {
                super.onBackPressed();
            } else {
                actualFragment = "MapFragment";
                selectedFragment = new MapFragment();
                if (selectedFragment != null) {
                    navigateToFragment(selectedFragment);
                    navigationView.setCheckedItem(R.id.nav_home);
                }
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Fragment selectedFragment = null; // hace referencia al fragment que vamos a abrir.
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                actualFragment = "SettingsFragment";
                bundle = new Bundle();// uso bundle para enviar datos del activity al fragment
                bundle.putParcelable("User", usuario);
                selectedFragment = new SettingsFragment();
                selectedFragment.setArguments(bundle);
                break;
            case R.id.nav_help:
                //Toast.makeText(this, "HELP", Toast.LENGTH_SHORT).show();
                sendMail();
                break;
            case R.id.nav_logout:
                //Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                doLogout();
                break;
            case R.id.nav_home:
                actualFragment = "MapFragment";
                selectedFragment = new MapFragment();
                System.out.println(selectedFragment.getId());
                break;
            case R.id.nav_weather:
                actualFragment = "WeatherFragment";
                selectedFragment = new WeatherFragment();
                System.out.println(selectedFragment.getId());
                break;
            default:
                actualFragment = "MapFragment";
                selectedFragment = new MapFragment();
                break;
        }
        if (selectedFragment != null) {
            navigateToFragment(selectedFragment);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Navega al fragment que recibe como paremetro
    private void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment, actualFragment).commit();
    }


    //Envia un mail a esa direccion electronica, con ese subject y nos permite elegir que
    // usar para enviar el mail
    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rviano662@alumos.iua.edu.ar"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        Intent mailer = Intent.createChooser(intent, getResources().getString(R.string.email_chooser));
        startActivity(mailer);
    }

    //Permite hacer logout. Por ahora solo setea a false la preferencia "is_logged_in"
    private void doLogout() {
        preferencesUtils.setUserIsLoggedIn(false);
        Intent intent = new Intent(this, RegisterLoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void saveEdit(User user) {
        //usuario = user;

        List<User> us = userRepository.getUserByName(preferencesUtils.getUserName());
        User u;
        if (us.size() != 0)
            u = us.get(0);
        else
            return;
        u.setUsername(user.getName());
        u.setEmail(user.getEmail());
        userRepository.updateUser(u);
        preferencesUtils.setUserName(u.getName());
        preferencesUtils.setUserEmail(u.getEmail());

        //preferencesUtils.set(user.getName());

    }

    //Abre la camara para tomar una foto
    @Override
    public Bitmap takePicture() {

        final CharSequence[] options = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Add Photo");
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        cameraIntent();

                        break;
                    case 1:
                        galleryIntent();
                        break;
                    default:
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        build.show();
        return photo;

    }

    @Override
    public void activateAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(getApplicationContext(), NotificationHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //2do parametro establece cuando la alarma va a ser tirada
        //3ro cada cuanto debe ser llamado. el intervalo.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 1000*60,1000*60,pendingIntent);
        Toast.makeText(HomeActivity.this, "Start alarm at " +hour + ":" + minute, Toast.LENGTH_LONG).show();
    }

    @Override
    public void deactivateAlarm(){
        Intent intent = new Intent(getApplicationContext(), NotificationHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(pendingIntent != null){
            alarmManager.cancel(pendingIntent);
            Log.d("Deactivating Alarm",pendingIntent.toString());
        }
    }
    //------------------------------SAVE DATA IN DATABASE-------------------------------------------
    private void saveData(List<WeatherFromApi> data) {
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
        preferencesUtils.setLastUpdate(new Date().getHours() + ":" + new Date().getMinutes());
        preferencesUtils.setActualTemp(convertUnits.formatTemperature(weather.get(0).getActualTemp(), preferencesUtils.getUnits()));
        preferencesUtils.setMaxTemp(convertUnits.formatTemperature(weather.get(0).getMaximum(), preferencesUtils.getUnits()));

    }


    // Método que chequea si hay conexión a Internet.
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            System.out.println("oops");
        }
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {// si saco una foto.
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            galleryAddPic();
            photo = imageBitmap;
        } else {
            if (requestCode == REQUEST_SELECT_PICTURE && resultCode == RESULT_OK) {// si la obtengo de la galeria.
                Bundle extras = data.getExtras();
                Uri uri = data.getData();// obtengo la uri de la foto.
                System.out.println(uri);
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        preferencesUtils.setUserPicture(image.getAbsolutePath());
        return image;
    }

    private void initUser() {
        usuario = new User("Rodrigoviano@hotmail.com", "1234", "Rodrigo Viano");
    }

    //------------------------------CONNECT TO API--------------------------------------------------
    @Override
    public void refreshWeather() {
        if (isNetworkConnected()) {
            g = new GPS(this);
            l = g.getLocation();

            if (l != null) {
                lat = l.getLatitude();
                lon = l.getLongitude();
                Toast.makeText(this, "LAT: " + lat + "LON: " + lon, Toast.LENGTH_LONG).show();
            }
            List<WeatherFromApi> list = new ArrayList<>();
            //strings 0 = latittud, strings 1 = longitud, strings 2 = units
            System.out.println("lat: " + lat + " lon: " + lon);

            String[] data = {"-31.41", "-64.18", "metric"};
            //list = new ArrayList<>();
            new GetWeatherFromService().execute(data);
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();
        }

    }


    private InputStream retrieveStream(String[] position, String type) throws IOException {
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/" + type + "?lat=" + position[0] + "&lon=" + position[1]  + "&APPID=" + Constants.UID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }


   /* private void showData(String data) {
        System.out.println("data: " + data);
    }*/

    private Gson gson;

   /* public WeatherFromApi weatherFromApi(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "weather");
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, WeatherFromApi.class);
    }

    public ForecastFromApi forecastWeather(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "forecast");
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, ForecastFromApi.class);

    }*/


    public class GetWeatherFromService extends AsyncTask<String, Void, List<WeatherFromApi>> {
        private Gson gson;
        private Weather weather;
        private WeatherDetails weatherDetails;
        private WeatherForecast weatherForecast;


        @Override
        protected List<WeatherFromApi> doInBackground(String... strings) {
            try {
                WeatherFromApi dto;
                List<WeatherFromApi> dtoList = new ArrayList<>();
                WeatherService weatherService = new WeatherService(getApplicationContext());
                //strings 0 = latittud, strings 1 = longitud, strings 2 = units
                String[] coords = {strings[0], strings[1]};
                dto = weatherService.weatherFromApi(coords, strings[2]);
                dtoList.add(dto);
                dtoList.addAll(weatherService.forecastWeather(coords, strings[2]).getWeatherFromApiList());
                saveData(dtoList);
                return dtoList;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<WeatherFromApi> weatherFromApis) {
            //super.onPostExecute(weatherFromApis);
            /*Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("WeatherFragment");
            getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
            Log.d("post execute", "detaching fragment");
            /*fragmentTransaction.remove(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();*/
        }


    }
}
