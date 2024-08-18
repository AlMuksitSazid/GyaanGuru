package com.gyaanguru.Account_details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.multidex.MultiDex;

import com.google.firebase.auth.FirebaseAuth;
import com.gyaanguru.Activities.MainActivity;
import com.gyaanguru.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
    //    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MultiDex.install(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}