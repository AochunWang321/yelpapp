package com.example.yelp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.yelp.ui.home.ReservationFragment;
import com.example.yelp.ui.home.ResultDetailsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yelp.databinding.ActivityMainBinding;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);

        /**
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_diary, R.id.navigation_quiz, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
         **/
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_byID = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
        if (fragment_byID!=null) {
            if (fragment_byID instanceof ResultDetailsFragment) {
                fragmentManager.beginTransaction().remove(fragment_byID).commit();
            } else if (fragment_byID instanceof ReservationFragment) {
                fragmentManager.beginTransaction().remove(fragment_byID).commit();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
}