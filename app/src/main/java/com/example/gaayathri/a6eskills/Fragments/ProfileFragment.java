package com.example.gaayathri.a6eskills.Fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gaayathri.a6eskills.Activites.AboutUsActivity;
import com.example.gaayathri.a6eskills.Activites.LoginActivity;
import com.example.gaayathri.a6eskills.Activites.SkillsActivity;
import com.example.gaayathri.a6eskills.Activites.Subskill2Activity;
import com.example.gaayathri.a6eskills.R;
import com.github.loadingview.LoadingView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    OkHttpClient client;

    Dialog editProfile;
    Dialog loadingDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getActivity().setTitle("My Profile");

        loadingDialog = new Dialog(getActivity());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();
        loadingDialog.show();

        client = new OkHttpClient();

        sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode

        String name = sharedpreferences.getString("username", "Username");
        String email = sharedpreferences.getString("primaryemail", "Email");
        String city = sharedpreferences.getString("city", "city");
        String phone = sharedpreferences.getString("phone", "Phone");
        String company = sharedpreferences.getString("company", "Company");
        String profilepicurl = sharedpreferences.getString("profilepicurl", "https://firebasestorage.googleapis.com/v0/b/seskills-master.appspot.com/o/boss.png?alt=media&token=7f1d6ae9-6d63-486a-8795-2229981b0989");
        String mainskilll = sharedpreferences.getString("mainSkill1", "skill 1");
        String mainskill2 = sharedpreferences.getString("mainSkill2", "skill 2");
        String subSkill1 = sharedpreferences.getString("subSkill1", "subskill 1");
        String subSkill2 = sharedpreferences.getString("subSkill2", "subskill 2");
        String sub2Skill1 = sharedpreferences.getString("sub2Skill1", "subskill 3");
        String sub2Skill2 = sharedpreferences.getString("sub2Skill2", "subskill 4");
        String level1 = sharedpreferences.getString("level1", "level 1");
        String level2 = sharedpreferences.getString("level2", "level 2");
        String secretkey = sharedpreferences.getString("secretkey", "secretkey");
        String textToBePlaced = sharedpreferences.getString("textToBePlaced", "-");

        TextView tvname = view.findViewById(R.id.tvname);
        TextView tvcity = view.findViewById(R.id.tvcity);
        TextView tvemail = view.findViewById(R.id.tvemail);
        TextView tvphone = view.findViewById(R.id.tvphone);
        TextView tvcompany = view.findViewById(R.id.tvcompany);
        TextView tvskill1 = view.findViewById(R.id.skill1);
        TextView tvskill2 = view.findViewById(R.id.skill2);
        TextView tvsubskill1 = view.findViewById(R.id.subskill1);
        TextView tvsubskill2 = view.findViewById(R.id.subskill2);
        TextView tvsub1skill1 = view.findViewById(R.id.sub1skill1);
        TextView tvsub1skill2 = view.findViewById(R.id.sub1skill2);
        TextView tvlevel1 = view.findViewById(R.id.level1);
        TextView tvlevel2 = view.findViewById(R.id.level2);
        TextView tvPoints = view.findViewById(R.id.tvPoints);

        CircularImageView circularImageView = view.findViewById(R.id.image);

        tvname.setText(name);
        tvcity.setText(city);
        tvemail.setText(email);
        tvphone.setText(phone);
        tvcompany.setText(company);
        tvskill1.setText(mainskilll);
        tvskill2.setText(mainskill2);
        tvsubskill1.setText(subSkill1);
        tvsubskill2.setText(subSkill2);
        tvsub1skill1.setText(sub2Skill1);
        tvsub1skill2.setText(sub2Skill2);
        tvlevel1.setText(level1);
        tvlevel2.setText(level2);
        tvPoints.setText(textToBePlaced);

        if(profilepicurl.equals("null")){
            //Toast.makeText(getActivity(), "url: " + profilepicurl, Toast.LENGTH_SHORT).show();
            profilepicurl = "https://firebasestorage.googleapis.com/v0/b/seskills-master.appspot.com/o/boss.png?alt=media&token=7f1d6ae9-6d63-486a-8795-2229981b0989";
        }

        final RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(getActivity()).load(profilepicurl).apply(options).into(circularImageView);

        editProfile = new Dialog(getActivity());
        editProfile.setContentView(R.layout.dialog_edit_profile);

        ImageButton btnProfileEdit = view.findViewById(R.id.btnProfileEdit);
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile.show();
                profileUpdate(secretkey);
            }
        });

        /*try {
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

            TextView tvPoints = view.findViewById(R.id.tvPoints);
            tvPoints.setText(textToBePlaced);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Cannot get your ratings", Toast.LENGTH_SHORT).show();
        } catch (JSONException ej) {
            ej.printStackTrace();
            //Toast.makeText(getActivity(), "Error: " + ej.toString(), Toast.LENGTH_SHORT).show();
        }*/

        loadingDialog.dismiss();

        return view;
    }

    /*private void setStarImage(View view, int ratingAgainstFive) {
        ImageView starIV = view.findViewById(R.id.ratingsIV);
        if (ratingAgainstFive == 1) {
            starIV.setImageResource(R.drawable.onestar);
        } else if (ratingAgainstFive == 2) {
            starIV.setImageResource(R.drawable.twostar);
        } else if (ratingAgainstFive == 3) {
            starIV.setImageResource(R.drawable.threestar);
        } else if (ratingAgainstFive == 4) {
            starIV.setImageResource(R.drawable.fourstar);
        } else if (ratingAgainstFive == 5) {
            starIV.setImageResource(R.drawable.fivestar);
        }
    }*/

    private int getRatingAgainstFive(int ratingaverage) {
        int mid = ratingaverage/20;
        int mid2 = Math.round(mid);
        return mid2;
    }

    private void profileUpdate(String secretkey) {

        Button btnCancel = editProfile.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile.dismiss();
            }
        });

        Button btnUpdate = editProfile.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_name = editProfile.findViewById(R.id.et_name);
                EditText et_email = editProfile.findViewById(R.id.et_email);
                EditText et_city = editProfile.findViewById(R.id.spCity);
                EditText et_company = editProfile.findViewById(R.id.spCompany);

                String updatedName = et_name.getText().toString();
                String updatedEmail = et_email.getText().toString();
                String updatedCity = et_city.getText().toString();
                String updatedCompany = et_company.getText().toString();

                RequestBody formBody = new FormBody.Builder()
                        .add("name", updatedName)
                        .add("email", updatedEmail)
                        .add("city", updatedCity)
                        .add("company", updatedCompany)
                        .build();


                final Request request = new Request.Builder()
                        .url("http://6eskills.com:8080/uat/api/v1/user/profile/details")
                        .put(formBody)
                        .addHeader("apikey", secretkey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        String mMessage = e.getMessage();
                        Log.w("failure Response", mMessage);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), mMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String mMessage = response.body().string();
                        int returnCode;
                        String returnName;
                        String returnEmail;
                        String returnCity;
                        String returnCompany;
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(mMessage);
                                returnCode = json.getInt("code");
                                returnName = json.getJSONObject("data").getString("username");
                                returnEmail = json.getJSONObject("data").getString("primaryemail");
                                returnCity = json.getJSONObject("data").getString("city");
                                returnCompany = json.getJSONObject("data").getString("company");

                                if (returnCode == 1004){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                                            ProfileFragment profileFragment = new ProfileFragment();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container,profileFragment);
                                            fragmentTransaction.commit();
                                        }
                                    });
                                    sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("username", returnName);
                                    editor.putString("primaryemail", returnEmail);
                                    editor.putString("city", returnCity);
                                    editor.putString("company", returnCompany);
                                    editor.apply();
                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
                //Toast.makeText(getActivity(), "Profile updated!!!", Toast.LENGTH_SHORT).show();
                editProfile.dismiss();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
