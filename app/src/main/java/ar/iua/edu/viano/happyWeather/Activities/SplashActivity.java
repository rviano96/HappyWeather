package ar.iua.edu.viano.happyWeather.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ar.iua.edu.viano.happyWeather.R;

public class SplashActivity extends AppCompatActivity {
    //El objeto AnimationDrawable se encargará de la animación de la imagen
    private AnimationDrawable animacion;
    private ImageView loading;
    //el objeto Animation de la transición de una activity hacia otra.
    private Animation transicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION permite ocultar el menú de navegación
        //View.SYSTEM_UI_FLAG_FULLSCREEN activa el modo fullscreen
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();
        loading = findViewById(R.id.loading);
        loading.setBackgroundResource(R.drawable.loading);
        animacion = (AnimationDrawable) loading.getBackground();
        animacion.start();
        transicion = AnimationUtils.loadAnimation(this,R.anim.mitransicion);
        loading.startAnimation(transicion);
        transicion.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                siguienteActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void siguienteActivity(){
        animacion.stop(); //Paramos el AnimationDrawable
        Intent intent = new Intent(this, RegisterLoginActivity.class); // Lanzamos HomeActivity
        startActivity(intent);
        finish(); //Finalizamos este activity
    }
}
