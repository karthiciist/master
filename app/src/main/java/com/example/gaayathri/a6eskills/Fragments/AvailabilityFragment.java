package com.example.gaayathri.a6eskills.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Activites.ChipsActivity;
import com.example.gaayathri.a6eskills.R;
import com.github.loadingview.LoadingView;
import com.squareup.timessquare.CalendarPickerView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AvailabilityFragment extends Fragment {

    // Integers
    static final int TIME_DIALOG_ID = 1111;
    private int hr;
    private int min;
    int currentHour;
    int currentMinute;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // Dialogs
    TimePickerDialog timePickerDialog;
    Dialog datePicker;
    Dialog loadingDialog;

    // Libraries
    OkHttpClient client;
    Calendar calendar;
    SharedPreferences sharedpreferences;

    // Strings
    String amPm;
    String dbStartTime, dbEndTime;

    // Views
    TextView noOfDays;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AvailabilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_availability, container, false);

        getActivity().setTitle("My Availability");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        client = new OkHttpClient();

        loadingDialog = new Dialog(getActivity());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
        LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
        loadingView.start();
        loadingDialog.show();

        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        datePicker = new Dialog(getActivity());
        datePicker.setContentView(R.layout.dialog_blackdates);

        Button btnUpdateDates = view.findViewById(R.id.btnUpdateDates);
        Button btnUpdateTime = view.findViewById(R.id.btnUpdateTime);
        Button btnBlackDates = view.findViewById(R.id.btnBlackDates);

        TextView tv_blackdates = view.findViewById(R.id.tv_blackdates);
        TextView tv_startdate = view.findViewById(R.id.tv_startdate);
        TextView tv_enddate = view.findViewById(R.id.tv_enddate);
        TextView tv_starttime = view.findViewById(R.id.tv_starttime);
        TextView tv_endtime = view.findViewById(R.id.tv_endtime);
        noOfDays = view.findViewById(R.id.noOfDays);

        String startDate = tv_startdate.getText().toString();
        String endDate = tv_enddate.getText().toString();
        String blackdays = "";

        new Thread() {
            public void run() {
                try {
                    // do the background process or any work that takes time to see progress dialog
                    tv_startdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                    (view, year, monthOfYear, dayOfMonth) -> tv_startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });
                    tv_enddate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                    (view, year, monthOfYear, dayOfMonth) -> tv_enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });
                    btnUpdateDates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.show();
                                }
                            });

                            new Thread() {
                                public void run() {
                                    try {
                                        // do the background process or any work that takes time to see progress dialog
                                        String startDate = tv_startdate.getText().toString();
                                        String endDate = tv_enddate.getText().toString();
                                        String blackdays = "";

                                        if (startDate.equals("Tap to choose") || endDate.equals("Tap to choose")){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "Select a valid date", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                            String secretCode = sharedpreferences.getString("secretkey", "");

                                            JSONArray blackdaysArray = new JSONArray();
                                            blackdaysArray.put(blackdays);

                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("from", startDate);
                                                jsonObject.put("to", endDate);
                                                jsonObject.put("from_time", "");
                                                jsonObject.put("to_time", "");
                                                jsonObject.put("blackdays", blackdaysArray);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            OkHttpClient client = new OkHttpClient();
                                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                            Request request = new Request.Builder()
                                                    .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                                    .post(body)
                                                    .addHeader("apikey", secretCode)
                                                    .addHeader("Content_type", "application/json")
                                                    .build();

                                            Response response = null;
                                            try {
                                                response = client.newCall(request).execute();
                                                String resStr = response.body().string();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();

                                                        String dateDiff = tv_startdate.getText().toString();
                                                        String dateDiff2 = tv_enddate.getText().toString();

                                                        try {
                                                            printDifference(dateDiff, dateDiff2);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                                /*String dateDiff = tv_startdate.getText().toString();
                                                String dateDiff2 = tv_enddate.getText().toString();

                                                printDifference(dateDiff, dateDiff2);*/

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }catch (Exception e) {
                                        Log.e("tag",e.getMessage());
                                    }
                                    // dismiss the progress dialog
                                    loadingDialog.dismiss();
                                }
                            }.start();

                            /*String startDate = tv_startdate.getText().toString();
                            String endDate = tv_enddate.getText().toString();
                            String blackdays = "";

                            if (startDate.equals("Tap to choose") || endDate.equals("Tap to choose")){
                                Toast.makeText(getActivity(), "Select a valid date", Toast.LENGTH_SHORT).show();
                            } else {
                                sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                String secretCode = sharedpreferences.getString("secretkey", "");

                                JSONArray blackdaysArray = new JSONArray();
                                blackdaysArray.put(blackdays);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("from", startDate);
                                    jsonObject.put("to", endDate);
                                    jsonObject.put("from_time", "");
                                    jsonObject.put("to_time", "");
                                    jsonObject.put("blackdays", blackdaysArray);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                OkHttpClient client = new OkHttpClient();
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                Request request = new Request.Builder()
                                        .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                        .post(body)
                                        .addHeader("api_secert", secretCode)
                                        .addHeader("Content_type", "application/json")
                                        .build();

                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();
                                    String resStr = response.body().string();
                                    Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();

                                    String dateDiff = tv_startdate.getText().toString();
                                    String dateDiff2 = tv_enddate.getText().toString();

                                    printDifference(dateDiff, dateDiff2);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    });

                    tv_starttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);

                            //createdDialog(0).show();

                            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = "PM";
                                    } else {
                                        amPm = "AM";
                                    }

                                    dbStartTime = String.format("%02d:%02d", hourOfDay, minutes);

                                    //tv_starttime.setText(String.format("%02d:%02d", hourOfDay, minutes));

                                    updateTime(view, hourOfDay, minutes);
                                }
                            }, currentHour, currentMinute, false);

                            timePickerDialog.show();
                        }
                    });
                    tv_endtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calendar = Calendar.getInstance();
                            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                            currentMinute = calendar.get(Calendar.MINUTE);

                            //createdDialog(0).show();

                            TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    if (hourOfDay >= 12) {
                                        amPm = "PM";
                                    } else {
                                        amPm = "AM";
                                    }

                                    dbEndTime = String.format("%02d:%02d", hourOfDay, minutes);
                                    //txtStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                                    updateTime1(view, hourOfDay, minutes);
                                }
                            }, currentHour, currentMinute, false);

                            timePickerDialog1.show();
                        }
                    });
                    btnUpdateTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String frmTime = tv_starttime.getText().toString();
                            String toTime = tv_endtime.getText().toString();

                            if (!(frmTime.equals("Tap to choose") & toTime.equals("Tap to choose"))){
                                String timeRange = frmTime + " - " + toTime;
                                TextView tvAvailableTimeRange = view.findViewById(R.id.tvAvailableTimeRange);
                                tvAvailableTimeRange.setText(timeRange);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.show();
                                }
                            });

                            new Thread() {
                                public void run() {
                                    try {
                                        // do the background process or any work that takes time to see progress dialog
                                        sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                        String secretCode = sharedpreferences.getString("secretkey", "");

                                        String startTime = tv_starttime.getText().toString();
                                        String endTime = tv_endtime.getText().toString();
                                        String blackdays = "";

                                        if (startTime.equals("Tap to choose") || endTime.equals("Tap to choose")){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "Please select a valid time", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            JSONArray blackdaysArray = new JSONArray();
                                            blackdaysArray.put(blackdays);

                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("from", "");
                                                jsonObject.put("to", "");
                                                jsonObject.put("from_time", dbStartTime);
                                                jsonObject.put("to_time", dbEndTime);
                                                jsonObject.put("blackdays", blackdaysArray);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            OkHttpClient client = new OkHttpClient();
                                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                            Request request = new Request.Builder()
                                                    .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                                    .post(body)
                                                    .addHeader("apikey", secretCode)
                                                    .addHeader("Content_type", "application/json")
                                                    .build();

                                            Response response = null;
                                            try {
                                                response = client.newCall(request).execute();
                                                String resStr = response.body().string();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }catch (Exception e) {
                                        Log.e("tag",e.getMessage());
                                    }
                                    // dismiss the progress dialog
                                    loadingDialog.dismiss();
                                }
                            }.start();

                            /*sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                            String secretCode = sharedpreferences.getString("secretkey", "");

                            String startTime = tv_starttime.getText().toString();
                            String endTime = tv_endtime.getText().toString();
                            String blackdays = "";

                            if (startTime.equals("Tap to choose") || endTime.equals("Tap to choose")){
                                Toast.makeText(getActivity(), "Please select a valid time", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray blackdaysArray = new JSONArray();
                                blackdaysArray.put(blackdays);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("from", "");
                                    jsonObject.put("to", "");
                                    jsonObject.put("from_time", dbStartTime);
                                    jsonObject.put("to_time", dbEndTime);
                                    jsonObject.put("blackdays", blackdaysArray);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                OkHttpClient client = new OkHttpClient();
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                Request request = new Request.Builder()
                                        .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                        .post(body)
                                        .addHeader("api_secert", secretCode)
                                        .addHeader("Content_type", "application/json")
                                        .build();

                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();
                                    String resStr = response.body().string();
                                    Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    });

                    tv_blackdates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePicker.show();

                            Calendar nextYear = Calendar.getInstance();
                            nextYear.add(Calendar.YEAR, 1);

                            CalendarPickerView calendar = datePicker.findViewById(R.id.calendar_view);
                            Date today = new Date();
                            calendar.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);

                            Button btnDone = datePicker.findViewById(R.id.btnDone);

                            btnDone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<Date> selectedDates = (ArrayList<Date>)calendar.getSelectedDates();
                                    ArrayList<String> finalDates = new ArrayList<>();

                                    for (int i= 0; i<selectedDates.size(); i++){
                                        Date dateString1 = selectedDates.get(i);
                                        String dateString = ConvertToDateStrig(dateString1);
                                        finalDates.add(dateString);
                                    }

                                    String finalDateString = dateOnly(finalDates.toString());
                                    tv_blackdates.setText(finalDateString);
                                    datePicker.dismiss();
                                }
                            });
                        }
                    });
                    btnBlackDates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.show();
                                }
                            });

                            new Thread() {
                                public void run() {
                                    try {
                                        // do the background process or any work that takes time to see progress dialog
                                        String blackDays = tv_blackdates.getText().toString();

                                        if (blackDays.equals("Tap to choose")){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "Select valid dates", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                            String secretCode = sharedpreferences.getString("secretkey", "");

                                            String blackdays = tv_blackdates.getText().toString();

                                            JSONArray blackdaysArray = new JSONArray();
                                            blackdaysArray.put(blackdays);

                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("from", "");
                                                jsonObject.put("to", "");
                                                jsonObject.put("from_time", "");
                                                jsonObject.put("to_time", "");
                                                jsonObject.put("blackdays", blackdaysArray);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            OkHttpClient client = new OkHttpClient();
                                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                            Request request = new Request.Builder()
                                                    .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                                    .post(body)
                                                    .addHeader("apikey", secretCode)
                                                    .addHeader("Content_type", "application/json")
                                                    .build();

                                            Response response = null;
                                            try {
                                                response = client.newCall(request).execute();
                                                String resStr = response.body().string();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                                                        String[] elements = blackdays.split(",");
                                                        List<String> fixedLenghtList = Arrays.asList(elements);
                                                        ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);
                                                        TextView tvNoOfBlackDays = view.findViewById(R.id.tvNoOfBlackDays);
                                                        tvNoOfBlackDays.setText(String.valueOf(listOfString.size()));
                                                    }
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        }catch (Exception e) {
                                        Log.e("tag",e.getMessage());
                                    }
                                    // dismiss the progress dialog
                                    loadingDialog.dismiss();
                                }
                            }.start();

                            /*String blackDays = tv_blackdates.getText().toString();

                            if (blackDays.equals("Tap to choose")){
                                Toast.makeText(getActivity(), "Select valid dates", Toast.LENGTH_SHORT).show();
                            } else {
                                sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                                String secretCode = sharedpreferences.getString("secretkey", "");

                                String blackdays = tv_blackdates.getText().toString();

                                JSONArray blackdaysArray = new JSONArray();
                                blackdaysArray.put(blackdays);

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("from", "");
                                    jsonObject.put("to", "");
                                    jsonObject.put("from_time", "");
                                    jsonObject.put("to_time", "");
                                    jsonObject.put("blackdays", blackdaysArray);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                OkHttpClient client = new OkHttpClient();
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                                Request request = new Request.Builder()
                                        .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                                        .post(body)
                                        .addHeader("api_secert", secretCode)
                                        .addHeader("Content_type", "application/json")
                                        .build();

                                Response response = null;
                                try {
                                    response = client.newCall(request).execute();
                                    String resStr = response.body().string();
                                    Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    });

                    if (!startDate.equals("Tap to choose") & !endDate.equals("Tap to choose")){
                        try {
                            printDifference(startDate, endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e) {
                    Log.e("tag",e.getMessage());
                }
                // dismiss the progress dialog
                loadingDialog.dismiss();
            }
        }.start();

        /*tv_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> tv_startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        tv_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> tv_enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnUpdateDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startDate = tv_startdate.getText().toString();
                String endDate = tv_enddate.getText().toString();
                String blackdays = "";

                if (startDate.equals("Tap to choose") || endDate.equals("Tap to choose")){
                    Toast.makeText(getActivity(), "Select a valid date", Toast.LENGTH_SHORT).show();
                } else {
                    sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                    String secretCode = sharedpreferences.getString("secretkey", "");

                    JSONArray blackdaysArray = new JSONArray();
                    blackdaysArray.put(blackdays);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("from", startDate);
                        jsonObject.put("to", endDate);
                        jsonObject.put("from_time", "");
                        jsonObject.put("to_time", "");
                        jsonObject.put("blackdays", blackdaysArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder()
                            .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                            .post(body)
                            .addHeader("api_secert", secretCode)
                            .addHeader("Content_type", "application/json")
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        String resStr = response.body().string();
                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();

                        String dateDiff = tv_startdate.getText().toString();
                        String dateDiff2 = tv_enddate.getText().toString();

                        printDifference(dateDiff, dateDiff2);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tv_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                //createdDialog(0).show();

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        dbStartTime = String.format("%02d:%02d", hourOfDay, minutes);

                        //tv_starttime.setText(String.format("%02d:%02d", hourOfDay, minutes));

                        updateTime(view, hourOfDay, minutes);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });
        tv_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                //createdDialog(0).show();

                TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        dbEndTime = String.format("%02d:%02d", hourOfDay, minutes);
                        //txtStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                        updateTime1(view, hourOfDay, minutes);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog1.show();
            }
        });
        btnUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                String secretCode = sharedpreferences.getString("secretkey", "");

                String startTime = tv_starttime.getText().toString();
                String endTime = tv_endtime.getText().toString();
                String blackdays = "";

                if (startTime.equals("Tap to choose") || endTime.equals("Tap to choose")){
                    Toast.makeText(getActivity(), "Please select a valid time", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray blackdaysArray = new JSONArray();
                    blackdaysArray.put(blackdays);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("from", "");
                        jsonObject.put("to", "");
                        jsonObject.put("from_time", dbStartTime);
                        jsonObject.put("to_time", dbEndTime);
                        jsonObject.put("blackdays", blackdaysArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder()
                            .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                            .post(body)
                            .addHeader("api_secert", secretCode)
                            .addHeader("Content_type", "application/json")
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        String resStr = response.body().string();
                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tv_blackdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                CalendarPickerView calendar = datePicker.findViewById(R.id.calendar_view);
                Date today = new Date();
                calendar.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);

                Button btnDone = datePicker.findViewById(R.id.btnDone);

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Date> selectedDates = (ArrayList<Date>)calendar.getSelectedDates();
                        ArrayList<String> finalDates = new ArrayList<>();

                        for (int i= 0; i<selectedDates.size(); i++){
                            Date dateString1 = selectedDates.get(i);
                            String dateString = ConvertToDateStrig(dateString1);
                            finalDates.add(dateString);
                        }

                        String finalDateString = dateOnly(finalDates.toString());
                        tv_blackdates.setText(finalDateString);
                        datePicker.dismiss();
                    }
                });
            }
        });
        btnBlackDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String blackDays = tv_blackdates.getText().toString();

                if (blackDays.equals("Tap to choose")){
                    Toast.makeText(getActivity(), "Select valid dates", Toast.LENGTH_SHORT).show();
                } else {
                    sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                    String secretCode = sharedpreferences.getString("secretkey", "");

                    String blackdays = tv_blackdates.getText().toString();

                    JSONArray blackdaysArray = new JSONArray();
                    blackdaysArray.put(blackdays);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("from", "");
                        jsonObject.put("to", "");
                        jsonObject.put("from_time", "");
                        jsonObject.put("to_time", "");
                        jsonObject.put("blackdays", blackdaysArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder()
                            .url("http://6eskills.com:8080/uat/api/v1/user/availability/create")
                            .post(body)
                            .addHeader("api_secert", secretCode)
                            .addHeader("Content_type", "application/json")
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        String resStr = response.body().string();
                        Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!startDate.equals("Tap to choose") & !endDate.equals("Tap to choose")){
            try {
                printDifference(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String dateOnly (String fulldate){

        String withoutTime = fulldate.replace("00:00:00", "");
        String withoutgmt = withoutTime.replace("GMT+05:30 ", "");
        String withoutMon = withoutgmt.replace("Mon", "");
        String withoutTue = withoutMon.replace("Tue", "");
        String withoutWed = withoutTue.replace("Wed", "");
        String withoutThu = withoutWed.replace("Thu", "");
        String withoutFri = withoutThu.replace("Fri", "");
        String withoutSat = withoutFri.replace("Sat", "");
        String withoutSun = withoutSat.replace("GMT+05:30 ", "");

        String withoutBracket1 = withoutSun.replace("[", "");
        String withoutBracket2 = withoutBracket1.replace("]", "");

        String dateWithoutSpace = withoutBracket2.replace(" 2018", "2018");
        String dateWithoutSpace1 = dateWithoutSpace.replace(" 2019", "2019");

        return dateWithoutSpace1;
    }

    private void updateTime(View view, int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        TextView tv_starttime = view.findViewById(R.id.tv_starttime);
        tv_starttime.setText(aTime);
    }

    private void updateTime1(View view, int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        TextView tv_endtime = view.findViewById(R.id.tv_endtime);
        tv_endtime.setText(aTime);
    }

    public static String ConvertToDateStrig(Date date){
        SimpleDateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
        return dateformat.format(date.getTime());
    }

    public void printDifference(String startDate, String endDate) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = simpleDateFormat.parse(startDate);
        Date date2 = simpleDateFormat.parse(endDate);

        long different = date1.getTime() - date2.getTime();
        long daysInMilli = 1000 * 60 * 60 * 24;
        long elapsedDays = different / daysInMilli;
        long unsignedElapsedDays = Math.abs(elapsedDays);

        noOfDays.setText(String.valueOf(unsignedElapsedDays));

    }
}
