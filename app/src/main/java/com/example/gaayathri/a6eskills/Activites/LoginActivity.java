package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;
import com.github.loadingview.LoadingDialog;
import com.github.loadingview.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    OkHttpClient client;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    JSONObject jsonProfile;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();

        sharedpreferences = LoginActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
        String loginStatus = sharedpreferences.getString("loginStatus", "0");
        //Toast.makeText(LoginActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
        if (loginStatus.equals("1")){
            Intent homeintent = new Intent(LoginActivity.this, ChipsActivity.class);
            startActivity(homeintent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            finish();
        } else {
            setContentView(R.layout.activity_login);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            client = new OkHttpClient();

            TextView forgotPassword = findViewById(R.id.forgotPassword);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterPopupActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                }
            });

            Button loginbtn = findViewById(R.id.btnLogin);
            loginbtn.setOnClickListener(v -> {

                loadingDialog.show();

                new Thread() {
                    public void run() {
                        try {
                            // do the background process or any work that takes time to see progress dialog
                            int countrycode = Integer.parseInt(GetCountryZipCode());
                            EditText et_phone = findViewById(R.id.et_phone);
                            EditText et_password = findViewById(R.id.et_password);
                            String phone = et_phone.getText().toString();
                            String password = et_password.getText().toString();

                            if(phone.trim().equals("") || password.trim().equals("") ) {
                                Toast.makeText(LoginActivity.this, "Empty Phone number or Password..!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // create your json here
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("phone", phone);
                                jsonObject.put("password", password);
                                jsonObject.put("countrycode", countrycode);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadingDialog.show();

                            //OkHttpClient client = new OkHttpClient();
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            // put your json here
                            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                            Request request = new Request.Builder()
                                    .url("http://6eskills.com:8080/uat/api/v1/user/login/form_submit")
                                    .post(body)
                                    .build();



                            Response response = null;
                            //Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                response = client.newCall(request).execute();
                                String resStr = response.body().string();

                                Log.v("log-def",resStr);
                                //Toast.makeText(LoginActivity.this, "response: " + resStr, Toast.LENGTH_SHORT).show();

                                jsonProfile = new JSONObject(resStr);
                                final Integer code = jsonProfile.getInt("code");

                                //loadingDialog.dismiss();


                                if (code.equals(1004)) {

                                    final String secretkey = jsonProfile.getJSONObject("data").getString("secretkey");

                                    //Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                                    Request profilerequest = new Request.Builder()
                                            .url("http://6eskills.com:8080/uat/api/v1/user/profile/details")
                                            .get()
                                            .addHeader("apikey", secretkey)
                                            .build();

                                    Response profileresponse = client.newCall(profilerequest).execute();
                                    String serverResponse = profileresponse.body().string();

                                    //Toast.makeText(this, serverResponse, Toast.LENGTH_SHORT).show();

                                    JSONObject profilejson = new JSONObject(serverResponse);
                                    final String profilecode = profilejson.getString("code");

                                    if (profilecode.equals("1004")) {

                                        String userid = null;
                                        String username = null;
                                        String primaryemail = null;
                                        String profilephone = null;
                                        String secondaryemail = null;
                                        String profilecountrycode = null;
                                        String resumeurl = null;
                                        String profilepicurl = null;
                                        String city = null;
                                        String company = null;

                                        try {
                                            userid = profilejson.getJSONObject("data").getString("userid");
                                            username = profilejson.getJSONObject("data").getString("username");
                                            profilephone = profilejson.getJSONObject("data").getString("phone");
                                            primaryemail = profilejson.getJSONObject("data").getString("primaryemail");
                                            secondaryemail = profilejson.getJSONObject("data").getString("secondaryemail");
                                            profilecountrycode = profilejson.getJSONObject("data").getString("countrycode");
                                            resumeurl = profilejson.getJSONObject("data").getString("resumeurl");
                                            profilepicurl = profilejson.getJSONObject("data").getString("profilepicurl");
                                            city = profilejson.getJSONObject("data").getString("city");
                                            company = profilejson.getJSONObject("data").getString("company");

                                            sharedpreferences = LoginActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("secretkey", secretkey);
                                            editor.putString("userid", userid);
                                            editor.putString("username", username);
                                            editor.putString("phone", profilephone);
                                            editor.putString("primaryemail", primaryemail);
                                            editor.putString("secondaryemail", secondaryemail);
                                            editor.putString("profilecountrycode", profilecountrycode);
                                            editor.putString("resumeurl", resumeurl);
                                            editor.putString("profilepicurl", profilepicurl);
                                            editor.putString("company", company);
                                            editor.putString("city", city);
                                            editor.putString("loginStatus", "1");
                                            editor.apply();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //loadingDialog.hide();

                                        Intent homeintent = new Intent(LoginActivity.this, ChipsActivity.class);
                                        startActivity(homeintent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                        finish();

                                    }


                                }
                                if (code.equals(1001)) {

                                    runOnUiThread(() -> Toast.makeText(LoginActivity.this,jsonProfile.optString("message"), Toast.LENGTH_SHORT).show());

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Check credentials and Internet Connection..!", Toast.LENGTH_SHORT).show());
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

            TextView btnSignup = findViewById(R.id.btnSignup);
            btnSignup.setOnClickListener(v -> {
                Intent homeintent = new Intent(LoginActivity.this, UserDataActivity.class);
                startActivity(homeintent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            });

            Button aboutUs = findViewById(R.id.aboutUs);
            aboutUs.setOnClickListener(v -> {
                Intent aboutusintent = new Intent(LoginActivity.this, AboutUsActivity.class);
                startActivity(aboutusintent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            });
        }

    }

    public String GetCountryZipCode() {

        String CountryID;
        String CountryZipCode = null;

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        return CountryZipCode;

    }
}
