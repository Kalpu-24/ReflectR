package net.kalp.reflectr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class WelcomeActivity extends AppCompatActivity {

    ImageView logo;
    TextView appName,appDesc;
    Animation anim;
    Button getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        if (getActionBar()!=null)
            getActionBar().hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setAppearanceLightStatusBars(true);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.appName);
        appDesc = findViewById(R.id.desc);
        getStarted = findViewById(R.id.getStarted);
        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(anim);
        appName.startAnimation(anim);
        appDesc.startAnimation(anim);
        getStarted.startAnimation(anim);
        getStarted.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginActivity.class)));
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}