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
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterPopupActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText phoneNo;
    Button sendOtp;

    String countryCode;
    String phone;
    String fullPhoneNumber;

    OkHttpClient client;
    SharedPreferences sharedpreferences;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_popup);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();

        client = new OkHttpClient();

        ccp = findViewById(R.id.ccpDialog);
        phoneNo = findViewById(R.id.et_phoneno);
        sendOtp = findViewById(R.id.btnSendOtp);

        sendOtp.setOnClickListener(v -> {

            loadingDialog.show();

            new Thread() {
                public void run() {
                    try {

                        countryCode = ccp.getSelectedCountryCode();
                        phone = phoneNo.getText().toString();

                        fullPhoneNumber = countryCode + phone;

                        // create your json here
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("phone", phone);
                            jsonObject.put("countrycode", countryCode);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            runOnUiThread(() ->Toast.makeText(RegisterPopupActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
                        }

                        //OkHttpClient client = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        // put your json here
                        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                        Request request = new Request.Builder()
                                .url("http://6eskills.com:8080/uat/api/v1/user/login/request/changepassword")
                                .post(body)
                                .build();

                        Response response = null;

                        try {
                            response = client.newCall(request).execute();
                            String resStr = response.body().string();

                            Log.v("log-def",resStr);
                            //Toast.makeText(RegisterPopupActivity.this, "response: " + resStr, Toast.LENGTH_SHORT).show();

                            JSONObject jsonProfile = new JSONObject(resStr);

                            final Integer code = jsonProfile.getInt("code");
                            final String passwordchangekey = jsonProfile.getString("passwordchangekey");

                            if (code.equals(1004)) {

                                sharedpreferences = RegisterPopupActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("passwordchangekey", passwordchangekey);
                                editor.apply();

                                Intent intent = new Intent(RegisterPopupActivity.this, ForgotPasswordOTPEntryActivity.class);
                                intent.putExtra("fullPhoneNumber", fullPhoneNumber);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);


                            } else {
                                runOnUiThread(() -> Toast.makeText(RegisterPopupActivity.this, "Mobile number not registered", Toast.LENGTH_SHORT).show());

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(RegisterPopupActivity.this, "Check credentials and Internet Connection..!", Toast.LENGTH_SHORT).show());

                        } catch (JSONException ej) {
                            ej.printStackTrace();
                        }

                    } catch (Exception e) {
                        Log.e("tag",e.getMessage());
                    }
                    // dismiss the progress dialog
                    loadingDialog.dismiss();
                }
            }.start();

        });

    }

}
