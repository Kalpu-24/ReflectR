package net.kalp.reflectr.logineduser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.kalp.reflectr.R;
import net.kalp.reflectr.logineduser.HomeFragemnts.AnalyticsFragment;
import net.kalp.reflectr.logineduser.HomeFragemnts.CalenderFragment;
import net.kalp.reflectr.logineduser.HomeFragemnts.JournalsFragment;
import net.kalp.reflectr.logineduser.HomeFragemnts.SettingsFragment;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton addJournalFAB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        if (getActionBar()!=null)
            getActionBar().hide();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        getWindow().setStatusBarColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_primary95,null));
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        addJournalFAB = findViewById(R.id.addJournalFAB);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        addJournalFAB.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), AddActivity.class));
        });
        bottomNavigationView.setSelectedItemId(R.id.journals);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.journals) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, JournalsFragment.class,null).commit();
                return true;
            } else if (item.getItemId() == R.id.analytics) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, AnalyticsFragment.class,null).commit();
                return true;
            } else if (item.getItemId() == R.id.calender) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, CalenderFragment.class,null).commit();
                return true;
            } else if(item.getItemId() == R.id.settings){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, SettingsFragment.class,null).commit();
                return true;
            } else {
                return false;
            }
        });
    }
}