package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Fragments.AboutusFragment;
import com.example.gaayathri.a6eskills.Fragments.AvailabilityFragment;
import com.example.gaayathri.a6eskills.Fragments.ProfileFragment;
import com.example.gaayathri.a6eskills.Fragments.HomeFragment;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.menu.DrawerAdapter;
import com.example.gaayathri.a6eskills.menu.DrawerItem;
import com.example.gaayathri.a6eskills.menu.SimpleItem;
import com.github.loadingview.LoadingView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChipsActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_HOME = 0;
    private static final int POS_PROFILE = 1;
    private static final int POS_AVAILABILITY = 2;
    private static final int POS_ABOUTUS = 3;
    private static final int POS_LOGOUT = 4;

    private String[] screenTitles;

    private SlidingRootNav slidingRootNav;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    Dialog loadingDialog;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chips);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Window window = ChipsActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ChipsActivity.this,R.color.colorPrimary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        //screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_PROFILE),
                createItemFor(POS_AVAILABILITY),
                createItemFor(POS_ABOUTUS),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);


        client = new OkHttpClient();

        sharedpreferences = ChipsActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
        String secretkey = sharedpreferences.getString("secretkey", "secretkey");

        try {
            Request ratingrequest = new Request.Builder()
                    .url("http://6eskills.com:8080/uat/api/v1/admin/rate/details")
                    .get()
                    .addHeader("apikey", secretkey)
                    .build();

            Response profileresponse = client.newCall(ratingrequest).execute();
            String serverResponse = profileresponse.body().string();

            JSONObject profilejson = new JSONObject(serverResponse);
            int ratingAgainstFive = getRatingAgainstFive(profilejson.getInt("rateaverage"));
            String textToBePlaced = ratingAgainstFive + " / " + "5";

            sharedpreferences = ChipsActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("textToBePlaced", textToBePlaced);
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getActivity(), "Cannot get your ratings", Toast.LENGTH_SHORT).show();
        } catch (JSONException ej) {
            ej.printStackTrace();
            //Toast.makeText(getActivity(), "Error: " + ej.toString(), Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onItemSelected(int position) {

        if (position == POS_HOME) {

            slidingRootNav.closeMenu();

            new Thread() {
                public void run() {
                    try {
                        // do the background process or any work that takes time to see progress dialog
                        slidingRootNav.closeMenu();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,new HomeFragment()).addToBackStack(null).commit();
                    }catch (Exception e) {
                        Log.e("tag",e.getMessage());
                    }
                    // dismiss the progress dialog
                    loadingDialog.dismiss();
                }
            }.start();

        }

        if (position == POS_PROFILE) {

            slidingRootNav.closeMenu();

            new Thread() {
                public void run() {
                    try {
                        // do the background process or any work that takes time to see progress dialog
                        slidingRootNav.closeMenu();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,new ProfileFragment()).addToBackStack(null).commit();
                    }catch (Exception e) {
                        Log.e("tag",e.getMessage());
                    }
                    // dismiss the progress dialog
                    loadingDialog.dismiss();
                }
            }.start();

        }

        if (position == POS_AVAILABILITY) {

            slidingRootNav.closeMenu();

            new Thread() {
                public void run() {
                    try {
                        // do the background process or any work that takes time to see progress dialog
                        slidingRootNav.closeMenu();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,new AvailabilityFragment()).addToBackStack(null).commit();
                    }catch (Exception e) {
                        Log.e("tag",e.getMessage());
                    }
                    // dismiss the progress dialog
                    loadingDialog.dismiss();
                }
            }.start();

        }


        if (position == POS_ABOUTUS) {

            slidingRootNav.closeMenu();

            new Thread() {
                public void run() {
                    try {
                        // do the background process or any work that takes time to see progress dialog
                        slidingRootNav.closeMenu();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container,new AboutusFragment()).addToBackStack(null).commit();
                    }catch (Exception e) {
                        Log.e("tag",e.getMessage());
                    }
                    // dismiss the progress dialog
                    loadingDialog.dismiss();
                }
            }.start();

        }

        if (position == POS_LOGOUT) {

            sharedpreferences = this.getSharedPreferences("mypref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();


            Intent homeintent = new Intent(ChipsActivity.this, LoginActivity.class);
            startActivity(homeintent);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
        }


    }


    private DrawerItem createItemFor(int position) {

        return new SimpleItem(screenTitles[position])
                .withTextTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.green_yellow));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private int getRatingAgainstFive(int ratingaverage) {
        int mid = ratingaverage/20;
        int mid2 = Math.round(mid);
        return mid2;
    }
}
