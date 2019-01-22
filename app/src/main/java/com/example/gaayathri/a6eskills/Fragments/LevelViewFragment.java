package com.example.gaayathri.a6eskills.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Activites.LoginActivity;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.adapter.GridListAdapter;
import com.github.loadingview.LoadingView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sonu on 08/02/17.
 */
public class LevelViewFragment extends Fragment {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<String> arrayList;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    OkHttpClient client;

    String name;
    String email;
    String city;
    String phone;
    String whatsappFlag;
    String profilePicDownloadUrl;
    String company;
    String blackCompanies;
    String resumeUrl;
    String skillsToDB;
    String level1;
    String level2;
    String password;
    String profilepicfilename;
    int countryCode;

    Dialog loadingDialog;

    public LevelViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;

        view = inflater.inflate(R.layout.list_view_fragment, container, false);

        Button finishButton = view.findViewById(R.id.show_button);
        finishButton.setText("FINISH");

        client = new OkHttpClient();

        loadingDialog = new Dialog(getActivity());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        onClickEvent(view);
    }

    private void loadListView(View view) {
        ListView listView = view.findViewById(R.id.list_view);
        arrayList = new ArrayList<>();

        arrayList.add("Architect");
        arrayList.add("Lead");
        arrayList.add("Senior Developer");
        arrayList.add("Developer");
        arrayList.add("Ops/Support engineer");

        adapter = new GridListAdapter(context, arrayList, true);
        listView.setAdapter(adapter);
    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.show_button).setOnClickListener(view1 -> {

            loadingDialog.show();

            new Thread() {
                public void run() {
                    try {

                        SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                        //Check if item is selected or not via size
                        if (selectedRows.size() < 3) {
                            StringBuilder stringBuilder = new StringBuilder();

                            ArrayList<String> skillsList = new ArrayList<String>();

                            //Loop to all the selected rows array
                            for (int i = 0; i < selectedRows.size(); i++) {

                                //Check if selected rows have value i.e. checked item
                                if (selectedRows.valueAt(i)) {

                                    //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                                    String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));

                                    //append the row label text
                                    stringBuilder.append(selectedRowLabel + "\n");

                                    skillsList.add(selectedRowLabel);

                                }
                            }

                            String level1 = skillsList.get(0);
                            String level2 = null;
                            try {
                                level2 = skillsList.get(1);
                            } catch (Exception e) {

                            }

                            sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("level1", level1);
                            editor.putString("level2", level2);
                            editor.apply();

                            String level1DB = null;
                            if (level1 == "Architect") {
                                level1DB = "arch";
                            } else if (level1 == "Lead") {
                                level1DB = "lead";
                            } else if (level1 == "Senior Developer") {
                                level1DB = "srdev";
                            } else if (level1 == "Developer") {
                                level1DB = "dev";
                            } else if (level1 == "Ops/Support engineer") {
                                level1DB = "opssupport";
                            }

                            String level2DB = null;
                            if (level1 == "Architect") {
                                level2DB = "arch";
                            } else if (level1 == "Lead") {
                                level2DB = "lead";
                            } else if (level1 == "Senior Developer") {
                                level2DB = "srdev";
                            } else if (level1 == "Developer") {
                                level2DB = "dev";
                            } else if (level1 == "Ops/Support engineer") {
                                level2DB = "opssupport";
                            }

                            //Toast.makeText(context, "Selected Rows\n" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();

                            sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                            name = sharedpreferences.getString("name", "");
                            email = sharedpreferences.getString("email", "");
                            city = sharedpreferences.getString("city", "");
                            phone = sharedpreferences.getString("phoneNo", "");
                            whatsappFlag = sharedpreferences.getString("whatsappFlag", "");
                            profilePicDownloadUrl = sharedpreferences.getString("profilePicDownloadUrl", "");
                            company = sharedpreferences.getString("company", "");
                            blackCompanies = sharedpreferences.getString("blackCompanies", "");
                            resumeUrl = sharedpreferences.getString("resumeDownloadUrl", "");
                            skillsToDB = sharedpreferences.getString("skillsToDB", "");
                            password = sharedpreferences.getString("password", "");
                            countryCode = sharedpreferences.getInt("countryCode", 91);
                            profilepicfilename = sharedpreferences.getString("profilepicfilename", "");
                            //String level1 = sharedpreferences.getString("level1", "");
                            //String level2 = sharedpreferences.getString("level2", "");

                            String levels = level1DB + "," + level2DB;

                            //  RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
                            RequestBody formBody = new FormBody.Builder()
                                    .add("name", name)
                                    .add("email", email)
                                    .add("city", city)
                                    .add("phone", phone)
                                    .add("whatsappflag", whatsappFlag)
                                    .add("notinterviewfor", blackCompanies)
                                    .add("skills", skillsToDB)
                                    .add("resume", resumeUrl)
                                    .add("password", password)
                                    .add("countrycode", String.valueOf(countryCode))
                                    .add("resumeurl", resumeUrl)
                                    .add("company", company)
                                    .add("profilepicurl", profilePicDownloadUrl)
                                    .add("level", levels)
                                    .add("profilepicfilename", profilepicfilename)
                                    .build();


                            final Request request = new Request.Builder()
                                    .url("http://6eskills.com:8080/uat/api/v1/user/signup/submit")
                                    .post(formBody)
                                    .addHeader("cache-control", "no-cache")
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    String mMessage = e.getMessage();
                                    Log.w("failure Response", mMessage);
                                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Technical Error.Kindly,Contact support !", Toast.LENGTH_SHORT).show());

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    String mMessage = response.body().string();
                                    if (response.isSuccessful()) {
                                        try {
                                            JSONObject json = new JSONObject(mMessage);
                                            final String serverResponse = json.getString("Your Index");
                                            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "placed", Toast.LENGTH_SHORT).show());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {

                                            sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("serverMsg", mMessage);
                                            editor.apply();

                                            if (mMessage.contains("Already")){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "Phone no already registered. Login to continue", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), "Please login to continue", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }
                                    });

                                    Intent homeintent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(homeintent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                                }
                            });

                        } else if (selectedRows.size() == 0) {
                            getActivity().runOnUiThread(() -> Toast.makeText(context, "Please select atleast one level", Toast.LENGTH_SHORT).show());
                        } else {
                            getActivity().runOnUiThread(() -> Toast.makeText(context, "You Can Select Only Maximum of 2 Levels", Toast.LENGTH_SHORT).show());
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

    private String getDBShortName(String skillToBeShort) {

        String skillShort = null;

        if (skillToBeShort == "DataScience"){
            skillShort = "1";
        } else if (skillToBeShort == "Data Engineering"){
            skillShort = "12";
        } else if (skillToBeShort == "DevOps"){
            skillShort = "25";
        } else if (skillToBeShort == "Automation"){
            skillShort = "71";
        } else if (skillToBeShort == "Analytics and Reporting"){
            skillShort = "42";
        } else if (skillToBeShort == "Infrastructure/Security/Identity Management"){
            skillShort = "49";
        } else if (skillToBeShort == "Statistical Programming"){
            skillShort = "2";
        } else if (skillToBeShort == "Machine Learning Models"){
            skillShort = "8";
        } else if (skillToBeShort == "Languages and Tools - Python"){
            skillShort = "4";
        } else if (skillToBeShort == "Languages and Tools - R"){
            skillShort = "5";
        } else if (skillToBeShort == "Languages and Tools - SAS"){
            skillShort = "6";
        } else if (skillToBeShort == "Languages and Tools - Sql/Database"){
            skillShort = "7";
        } else if (skillToBeShort == "Statistical Packages"){
            skillShort = "9";
        } else if (skillToBeShort == "Deep Learning"){
            skillShort = "10";
        } else if (skillToBeShort == "Complex and Unstructured Data, including Text"){
            skillShort = "11";
        } else if (skillToBeShort == "Dimensional Modeling"){
            skillShort = "13";
        } else if (skillToBeShort == "Platforms - RedShift/AWS/Linux/Jenkins"){
            skillShort = "14";
        } else if (skillToBeShort == "Platforms - SQLServer/Azure"){
            skillShort = "15";
        } else if (skillToBeShort == "Platforms - Python"){
            skillShort = "16";
        } else if (skillToBeShort == "API Creation to input/export data"){
            skillShort = "17";
        } else if (skillToBeShort == "DevOps background"){
            skillShort = "18";
        } else if (skillToBeShort == "Visualization Experience"){
            skillShort = "19";
        } else if (skillToBeShort == "Data Warehousing - Informatica"){
            skillShort = "21";
        } else if (skillToBeShort == "Data Warehousing - Ab Initio"){
            skillShort = "22";
        } else if (skillToBeShort == "Data Warehousing - Data Stage"){
            skillShort = "23";
        } else if (skillToBeShort == "Java / AWS"){
            skillShort = "26";
        } else if (skillToBeShort == ".NET / Azure"){
            skillShort = "27";
        } else if (skillToBeShort == "Pivotal Cloud Foundry"){
            skillShort = "28";
        } else if (skillToBeShort == "MEAN ( AngJs)"){
            skillShort = "29";
        } else if (skillToBeShort == "MERN ( ReactJs)"){
            skillShort = "30";
        } else if (skillToBeShort == "Testing"){
            skillShort = "31";
        } else if (skillToBeShort == "IoT"){
            skillShort = "33";
        } else if (skillToBeShort == "BlockChain"){
            skillShort = "34";
        } else if (skillToBeShort == "RPA using UIPath"){
            skillShort = "36";
        } else if (skillToBeShort == "RPA using BluePrism"){
            skillShort = "37";
        } else if (skillToBeShort == "RPA using AutomationAnywhere"){
            skillShort = "38";
        } else if (skillToBeShort == "Artificial Intelligence"){
            skillShort = "39";
        } else if (skillToBeShort == "Machine Learning"){
            skillShort = "40";
        } else if (skillToBeShort == "Connected Car Technologies"){
            skillShort = "41";
        } else if (skillToBeShort == "Oracle BI"){
            skillShort = "43";
        } else if (skillToBeShort == "SAP BI"){
            skillShort = "44";
        } else if (skillToBeShort == "Tableau"){
            skillShort = "45";
        } else if (skillToBeShort == "Qlikview"){
            skillShort = "46";
        } else if (skillToBeShort == "R"){
            skillShort = "47";
        } else if (skillToBeShort == "Microsoft SSIS/SSAS/SSRS"){
            skillShort = "48";
        } else if (skillToBeShort == "Sys Admin - Linux"){
            skillShort = "51";
        } else if (skillToBeShort == "Sys Admin - Windows"){
            skillShort = "52";
        } else if (skillToBeShort == "Database Admin - Oracle"){
            skillShort = "54";
        } else if (skillToBeShort == "Database Admin - DB/2"){
            skillShort = "55";
        } else if (skillToBeShort == "Database Admin - MySQL"){
            skillShort = "56";
        } else if (skillToBeShort == "Database Admin - SQLServer"){
            skillShort = "57";
        } else if (skillToBeShort == "AppServer Admin - Oracle Weblogic"){
            skillShort = "59";
        } else if (skillToBeShort == "AppServer Admin - Websphere"){
            skillShort = "60";
        } else if (skillToBeShort == "AppServer Admin - JBOSS"){
            skillShort = "61";
        } else if (skillToBeShort == "Firewalls, Vulnerability, Web Monitoring"){
            skillShort = "62";
        } else if (skillToBeShort == "Digital Identity Management - ForgeRock"){
            skillShort = "64";
        } else if (skillToBeShort == "Digital Identity Management - Oracle"){
            skillShort = "65";
        } else if (skillToBeShort == "Digital Identity Management - Ping Identity"){
            skillShort = "66";
        } else if (skillToBeShort == "Digital Rights Management - Amazon DRM"){
            skillShort = "68";
        } else if (skillToBeShort == "Digital Rights Management - Apple Fairplay"){
            skillShort = "69";
        } else if (skillToBeShort == "Digital Rights Management - Adobe ADEPT"){
            skillShort = "70";
        }

        return skillShort;
    }
}
