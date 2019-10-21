package ar.iua.edu.viano.happyWeather.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.UI.fragments.LoginFragment;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.fragments.RegisterFragment;
import ar.iua.edu.viano.happyWeather.Model.User;

public class RegisterLoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener {
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    LoginFragment fragmentLogin = new LoginFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    private PreferencesUtils preferencesUtils;
    private int actualFragment = -1; //0 si es login, 1 si es register, -1 otherwiese
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        getSupportActionBar().hide();
        View decorView = getWindow().getDecorView();
        //View.SYSTEM_UI_FLAG_FULLSCREEN activa el modo fullscreen
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        fragmentTransaction = fragmentManager.beginTransaction();
        actualFragment = 0;
        fragmentTransaction.add(R.id.fragment, fragmentLogin, "Login Fragment");
        fragmentTransaction.commit();
        preferencesUtils = new PreferencesUtils(this);
    }

    @Override
    public void navigateToRegisterScreen() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentRegister = new RegisterFragment();
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment, fragmentRegister).commit();
        actualFragment = 1;
    }

    @Override
    public void navigateToLoginScreen() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentLogin = new LoginFragment();
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment, fragmentLogin).commit();
        actualFragment = 0;
    }

    @Override
    public void onBackPressed() {
        if(actualFragment==0)
            super.onBackPressed();
        else
            navigateToLoginScreen();
    }

    @Override
    public void doRegister(User user) {
        doLogin(user);
    }

    @Override
    public void doLogin(User user) {
        preferencesUtils.setUserEmail(user.getEmail());
        preferencesUtils.setUserName(user.getName());
        preferencesUtils.setUserIsLoggedIn(true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.USER_DATA, user);
        startActivity(intent);
        finish();
    }

}
