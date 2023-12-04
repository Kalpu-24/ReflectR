package net.kalp.reflectr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import net.kalp.reflectr.logineduser.HomeActivity;
import net.kalp.reflectr.logineduser.ProfileFormActivity;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView bg,fg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //hiding action bar and setting status bar and nav bar color
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


        //initializing views
        bg=findViewById(R.id.bg);
        fg=findViewById(R.id.fg);


        //starting animations
        bg.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        fg.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));


        //check for network
        if(haveNetworkConnection()) {


            //redirecting to page after 3 sec
            Runnable runnable = () -> {
                try {
                    Thread.sleep(3000);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


                    //if no user send to welcome page
                    if (firebaseUser==null) {
                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        finish();
                    }else {


                        //get profile of user and check if profile is completed
                        FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {


                                    //if profile is completed send to home page
                                    if (Boolean.TRUE.equals(task.getResult().getBoolean("is_profile_completed"))) {
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();


                                        //else send to profile form
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), ProfileFormActivity.class));
                                        finish();
                                    }
                                } else {
                                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }


        //if network not available send to no network page
        else startActivity(new Intent(getApplicationContext(),NoNetworkActivity.class));
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                startActivity(new Intent(getApplicationContext(),NoNetworkActivity.class));
                finish();
            }
        };
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }


    //fun to check for network
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo!=null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                if (netInfo.isConnectedOrConnecting())
                    haveConnectedWifi = true;
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                if (netInfo.isConnectedOrConnecting())
                    haveConnectedMobile = true;
        }
        return haveConnectedMobile || haveConnectedWifi;
    }
}