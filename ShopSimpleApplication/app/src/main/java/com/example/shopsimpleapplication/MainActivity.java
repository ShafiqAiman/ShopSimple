package com.example.shopsimpleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

// to create the splashscreen
public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;


    //variables
    Animation topAnim;
    Animation bottomAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.imageView);
        slogan = findViewById(R.id.textView);

        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity( new Intent(getApplicationContext(), Login.class));

                         Intent intent = new Intent(MainActivity.this,Login.class);
                         startActivity(intent);
                         finish();

           }

            //private void startActivity(Intent intent) {
            //}
        },SPLASH_SCREEN);


    }
}