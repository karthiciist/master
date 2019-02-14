package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;
import com.github.loadingview.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RedeemActivity extends AppCompatActivity {

    Dialog redeemDialog;
    OkHttpClient client;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        Window window = RedeemActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(RedeemActivity.this, R.color.colorPrimary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        redeemDialog = new Dialog(RedeemActivity.this);
        redeemDialog.setContentView(R.layout.dialog_redeem);

        TextView earnedPoints = findViewById(R.id.earnedPoints);
        int totalEarnedPoints = Integer.valueOf(earnedPoints.getText().toString());

        Button btnNewRequest = findViewById(R.id.btnNewRequest);
        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemDialog.show();
            }
        });

        Button btnRedeem = redeemDialog.findViewById(R.id.btnRedeem);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_pointsToRedeem = redeemDialog.findViewById(R.id.et_pointsToRedeem);
                Double pointsToRedeem = Double.valueOf(et_pointsToRedeem.getText().toString());

                loadingDialog = new Dialog(RedeemActivity.this);
                loadingDialog.setContentView(R.layout.dialog_loading);
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
                LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
                loadingView.start();

                client = new OkHttpClient();

                new Thread() {

                    public void run() {

                            try {
                                JSONObject jsonobject = new JSONObject();


                                    SharedPreferences sharedpreferences = getSharedPreferences("mypref", 0); // 0 - for private mode
                                    String secretkey = sharedpreferences.getString("secretkey", "");

                                //OkHttpClient client = new OkHttpClient();
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                // put your json here
                                RequestBody body = RequestBody.create(JSON, jsonobject.toString());
                                    Request profilerequest = new Request.Builder()
                                            .url("http://6eskills.com:8080/uat/api/v1/user/redeem/new/request")
                                            .get()
                                            .addHeader("apikey", secretkey).post(body)
                                            .build();

                                    Response profileresponse = client.newCall(profilerequest).execute();
                                    String serverResponse = profileresponse.body().string();

                                    JSONObject profilejson = new JSONObject(serverResponse);
                                    final Integer profilecode = profilejson.getInt("code");


                                if (profilecode == 1001) {
                                    runOnUiThread(() -> {
                                        try {
                                            Toast.makeText(RedeemActivity.this, profilejson.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    });
                                }

                                if (profilecode == 1004) {

                                    runOnUiThread(() -> {
                                        try {
                                            Toast.makeText(RedeemActivity.this,profilejson.getString("message") , Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    });
                                    Intent homeintent = new Intent(RedeemActivity.this, LoginActivity.class);
                                    startActivity(homeintent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    finish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(RedeemActivity.this, "Check credentials and Internet Connection..!", Toast.LENGTH_SHORT).show());
                            }

                        loadingDialog.dismiss();
                    }
                }.start();











                if (pointsToRedeem < totalEarnedPoints) {
                    Toast.makeText(RedeemActivity.this, "Requested", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RedeemActivity.this, "Requested amount cannot be greater than Earned Points", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCancel = redeemDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemDialog.dismiss();
            }
        });


    }


}
