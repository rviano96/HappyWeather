package ar.iua.edu.viano.happyWeather.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Persistence.Database.Users.UserRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.fragments.LoginFragment;
import ar.iua.edu.viano.happyWeather.UI.fragments.RegisterFragment;
public class RegisterLoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener {
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    LoginFragment fragmentLogin = new LoginFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    private PreferencesUtils preferencesUtils;
    private int actualFragment = -1; //0 si es login, 1 si es register, -1 otherwiese
    //BDD
    private UserRepository userRepository;

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
        userRepository = new UserRepository(getApplication());
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
        if (actualFragment == 0)
            super.onBackPressed();
        else
            navigateToLoginScreen();
    }

    @Override
    public void doRegister(User user) {
       // byte[] data = user.getPsw().getBytes();
        //String text = Base64.encodeToString(data, Base64.DEFAULT);
        //String password = user.getPsw();
        //user.setPsw(text);
        userRepository.insert(user);
        //user.setPsw(password);
        doLogin(user);
    }

    @Override
    public void doLogin(User user) {
        List<User> u = userRepository.getUserByName(user.getUsername());
        if (u.size() > 0) {
            //byte[] data = user.getPsw().getBytes();
            //String text = Base64.encodeToString(data, Base64.DEFAULT);
            if (u.get(0).getPsw().equals(user.getPsw())) {
                user = u.get(0);
                preferencesUtils.setUserEmail(user.getEmail());
                preferencesUtils.setUserName(user.getUsername());
                preferencesUtils.setUserIsLoggedIn(true);
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Constants.USER_DATA, user);
                startActivity(intent);
                finish();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantLogin), Toast.LENGTH_LONG);
                View view = toast.getView();
                //Gets the actual oval background of the Toast then sets the colour filter
                view.getBackground().setColorFilter(getResources().getColor(R.color.error), PorterDuff.Mode.SRC_IN);
                //Gets the TextView from the Toast so it can be editted
                TextView txt = view.findViewById(android.R.id.message);
                txt.setTextColor(getResources().getColor(R.color.black));
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantLogin), Toast.LENGTH_LONG);
            View view = toast.getView();
            //Gets the actual oval background of the Toast then sets the colour filter
            view.getBackground().setColorFilter(getResources().getColor(R.color.error), PorterDuff.Mode.SRC_IN);
            //Gets the TextView from the Toast so it can be editted
            TextView txt = view.findViewById(android.R.id.message);
            txt.setTextColor(getResources().getColor(R.color.black));
            toast.show();
        }

    }


}
