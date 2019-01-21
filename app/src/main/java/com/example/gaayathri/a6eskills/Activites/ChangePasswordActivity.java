package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    OkHttpClient client;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);

        client = new OkHttpClient();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();

        EditText et_password = findViewById(R.id.et_password);
        EditText et_passwordconfirm = findViewById(R.id.et_passwordconfirm);

        String password = et_password.getText().toString();
        String confirmPassword = et_passwordconfirm.getText().toString();

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.equals(confirmPassword)) {

                    loadingDialog.show();

                    new Thread() {
                        public void run() {
                            try {
                                // do the background process or any work that takes time to see progress dialog
                                EditText et_password = findViewById(R.id.et_password);
                                String password = et_password.getText().toString();

                                sharedpreferences = ChangePasswordActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                                String passwordchangekey = sharedpreferences.getString("passwordchangekey", "");

                                // create your json here
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("key", passwordchangekey);
                                    jsonObject.put("password", password);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                Request request = new Request.Builder()
                                        .url("http://6eskills.com:8080/uat/api/v1/user/login/request/changepassword")
                                        .put(body)
                                        .build();

                                Response response = null;

                                try {
                                    response = client.newCall(request).execute();
                                    String resStr = response.body().string();

                                    Log.v("log-def",resStr);

                                    JSONObject jsonProfile = new JSONObject(resStr);
                                    final Integer code = jsonProfile.getInt("code");

                                    if (code.equals(1004)) {

                                        runOnUiThread(() -> Toast.makeText(ChangePasswordActivity.this, "Password Changed. Kindly Login", Toast.LENGTH_SHORT).show());

                                        Intent homeintent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        startActivity(homeintent);
                                        ChangePasswordActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                        finish();

                                    }
                                    if (code.equals(1001)) {
                                        runOnUiThread(() -> Toast.makeText(ChangePasswordActivity.this,jsonProfile.optString("message"), Toast.LENGTH_SHORT).show());
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    runOnUiThread(() -> Toast.makeText(ChangePasswordActivity.this, "Check credentials and Internet Connection..!", Toast.LENGTH_SHORT).show());
                                } catch (JSONException ej) {
                                    ej.printStackTrace();
                                }
                            } catch (Exception e) {
                                Log.e("tag",e.getMessage());
                            }
                            loadingDialog.dismiss();
                        }
                    }.start();

                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Password is not Matching !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
