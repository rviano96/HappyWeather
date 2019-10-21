package ar.iua.edu.viano.happyWeather.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.fragments.MapFragment;
import ar.iua.edu.viano.happyWeather.UI.fragments.SettingsFragment;
import ar.iua.edu.viano.happyWeather.UI.fragments.WeatherFragment;
import ar.iua.edu.viano.happyWeather.Model.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.EditButtonListener {
    private User usuario;
    private DrawerLayout drawer;
    private String actualFragment = null;
    //referencia al fragment que vamos a abrir
    Fragment selectedFragment = null;
    NavigationView navigationView = null;
    Bundle bundle = null;
    Bitmap photo = null;
    static final int REQUEST_TAKE_PHOTO = 0,  REQUEST_SELECT_PICTURE = 1, REQUEST_CAMERA = 2;
    String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(actualFragment == null){
            actualFragment="MapFragment";
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
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
            if(actualFragment.equals("MapFragment")){
                super.onBackPressed();
            }else{
                actualFragment = "MapFragment";
                selectedFragment = new MapFragment();
                if (selectedFragment != null){
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
        if (selectedFragment != null){
            navigateToFragment(selectedFragment);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Navega al fragment que recibe como paremetro
    private void navigateToFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    //Envia un mail a esa direccion electronica, con ese subject y nos permite elegir que
    // usar para enviar el mail
    private void sendMail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rviano662@alumos.iua.edu.ar"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        Intent mailer = Intent.createChooser(intent, getResources().getString(R.string.email_chooser));
        startActivity(mailer);
    }

    //Permite hacer logout. Por ahora solo es un codigo dummy
    private void doLogout(){
        Intent intent = new Intent(this, RegisterLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void saveEdit(User user) {
        usuario = user;
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
               switch (i){
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

    private void cameraIntent(){
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

    private void galleryIntent(){
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
        }else{
            if (requestCode == REQUEST_SELECT_PICTURE && resultCode == RESULT_OK) {// si la obtengo de la galeria.
                Bundle extras = data.getExtras();
                Uri uri = data.getData();// obtengo la uri de la foto.
                System.out.println(uri);
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
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
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void initUser(){
        usuario = new User("Rodrigoviano@hotmail.com", "1234", "Rodrigo Viano");
    }
}
