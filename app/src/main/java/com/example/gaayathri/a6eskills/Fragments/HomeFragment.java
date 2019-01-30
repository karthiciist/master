package com.example.gaayathri.a6eskills.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.Utils.MyListAdapter;
import com.example.gaayathri.a6eskills.Utils.UIUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    Dialog scheduleDialod;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    OkHttpClient client;


    public HomeFragment() {
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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("6e Skills");

        scheduleDialod = new Dialog(getActivity());
        scheduleDialod.setContentView(R.layout.dialog_schedule);

        sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
        String name = sharedpreferences.getString("username", "Username");
        String profilepicurl = sharedpreferences.getString("profilepicurl", "https://firebasestorage.googleapis.com/v0/b/seskills-master.appspot.com/o/boss.png?alt=media&token=7f1d6ae9-6d63-486a-8795-2229981b0989");

        ListView listView = view.findViewById(R.id.list_view);
        client = new OkHttpClient();


        if(profilepicurl.equals("")){
            //Toast.makeText(getActivity(), "url: " + profilepicurl, Toast.LENGTH_SHORT).show();
            profilepicurl = "https://firebasestorage.googleapis.com/v0/b/seskills-master.appspot.com/o/boss.png?alt=media&token=7f1d6ae9-6d63-486a-8795-2229981b0989";
        }

        TextView tvname = view.findViewById(R.id.name);
        tvname.setText("Welcome back, " + name);

        CircularImageView CircularImageView = view.findViewById(R.id.image);

        final RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(getActivity()).load(profilepicurl).apply(options).into(CircularImageView);

        //Toast.makeText(getActivity(), "URL: " + profilepicurl, Toast.LENGTH_SHORT).show();


        Button btnMore = view.findViewById(R.id.button8);
        btnMore.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,profileFragment);
            fragmentTransaction.commit();
        });


        Context cont = getActivity();

        AsyncTaskRunner runner = new AsyncTaskRunner(cont, view);
        runner.execute();


        return view;
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;
        private Context mContext;
        private View rootView;

        public AsyncTaskRunner(Context context, View rootView){
            this.mContext=context;
            this.rootView=rootView;
        }

        @Override
        protected String doInBackground(String... params) {

            Request profilerequest = new Request.Builder()
                    .url("http://6eskills.com:8080/uat/api/v1/user/interview/my/interview")
                    .get()
                    .addHeader("apikey", "$2a$10$QznwjFXyXthudTbX84kYHuSfi2RMUZPdVq1FDZ1NR/MUA6N5udOF2")
                    .build();

            // Getting response from the client
            Response profileresponse = null;
            String serverResponse = null;
            try {
                profileresponse = client.newCall(profilerequest).execute();
                serverResponse = profileresponse.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Looping through interviews and getting data
            ArrayList<String> nameList = new ArrayList<String>();
            ArrayList<String> agidList = new ArrayList<String>();
            ArrayList<String> interviewidList = new ArrayList<String>();
            ArrayList<String> titleList = new ArrayList<String>();
            ArrayList<String> companyidList = new ArrayList<String>();
            ArrayList<String> companynameList = new ArrayList<String>();
            ArrayList<String> descList = new ArrayList<String>();
            ArrayList<String> statusList = new ArrayList<String>();
            ArrayList<String> positionfromList = new ArrayList<String>();
            ArrayList<String> positiontoList = new ArrayList<String>();
            ArrayList<String> packegefromList = new ArrayList<String>();
            ArrayList<String> packegetoList = new ArrayList<String>();
            ArrayList<String> currencycodeList = new ArrayList<String>();
            ArrayList<String> montlyoryearlyList = new ArrayList<String>();
            ArrayList<String> fromdateList = new ArrayList<String>();
            ArrayList<String> todateList = new ArrayList<String>();
            ArrayList<String> skillidList = new ArrayList<String>();
            ArrayList<String> skillnameList = new ArrayList<String>();

            try {

                JSONArray jsonArray = new JSONArray(serverResponse) ;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);

                    String name = explrObject.getString("skillname");
                    String agid = explrObject.getString("agid");
                    String interviewid = explrObject.getString("interviewid");
                    String title = explrObject.getString("title");
                    String companyid = explrObject.getString("companyid");
                    String companyname = explrObject.getString("companyname");
                    String desc = explrObject.getString("desc");
                    String status = explrObject.getString("status");
                    String positionfrom = explrObject.getString("positionfrom");
                    String positionto = explrObject.getString("positionto");
                    String packegefrom = explrObject.getString("packegefrom");
                    String packegeto = explrObject.getString("packegeto");
                    String currencycode = explrObject.getString("currencycode");
                    String montlyoryearly = explrObject.getString("montlyoryearly");
                    String fromdate = explrObject.getString("fromdate");
                    String todate = explrObject.getString("todate");
                    String skillid = explrObject.getString("skillid");
                    String skillname = explrObject.getString("skillname");

                    nameList.add(name);
                    agidList.add(agid);
                    interviewidList.add(interviewid);
                    titleList.add(title);
                    companyidList.add(companyid);
                    companynameList.add(companyname);
                    descList.add(desc);
                    statusList.add(status);
                    positionfromList.add(positionfrom);
                    positiontoList.add(positionto);
                    packegefromList.add(packegefrom);
                    packegetoList.add(packegeto);
                    currencycodeList.add(currencycode);
                    montlyoryearlyList.add(montlyoryearly);
                    fromdateList.add(fromdate);
                    todateList.add(todate);
                    skillidList.add(skillid);
                    skillnameList.add(skillname);

                    Log.d(name,"Output");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Setting array adapter
            final MyListAdapter myListAdapter = new MyListAdapter(getActivity(), nameList, agidList, interviewidList, titleList, companyidList, companynameList, descList, statusList, positionfromList, positiontoList, packegefromList, packegetoList, currencycodeList, montlyoryearlyList, fromdateList, todateList, skillidList, skillnameList);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView listView = rootView.findViewById(R.id.list_view);
                    listView.setAdapter(myListAdapter);
                    UIUtils.setListViewHeightBasedOnItems(listView);
                }
            });


        /*ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, items);
        listView.setAdapter(mArrayAdapter);*/
            return "good";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Interviews loading", "Please wait");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
