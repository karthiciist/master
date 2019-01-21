package com.example.gaayathri.a6eskills.Activites;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.HttpHandler;
import com.example.gaayathri.a6eskills.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    //private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "https://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        transactionList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TestActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //HttpHandler sh = new HttpHandler();



            String jsonSample = "{{\"agid\": 1,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},{\"agid\": 2,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},{\"agid\": 3,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},{\"agid\": 4,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},{\"agid\": 5,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},{\"agid\": 6,\"userid\": \"cvar\",\"amount\": 500,\"activity\": [],\"createdon\": \"2019-01-20T00:00:00+05:30\"},}";

            JSONObject jsonObj = null;

            try {
                jsonObj = new JSONObject(jsonSample);
            } catch (JSONException e) {
                e.printStackTrace();
            }




            // Making a request to url and getting response
            //String jsonStr = sh.makeServiceCall(url);

            //Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonSample != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonSample);

                    // Getting JSON Array node
                    //JSONArray transactions = new JSONArray(jsonSample);

                    JSONArray transactions = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < transactions.length(); i++) {
                        JSONObject c = transactions.getJSONObject(i);

                        String agid = c.getString("agid");
                        String userid = c.getString("userid");
                        String amount = c.getString("amount");

                        // tmp hash map for single contact
                        HashMap<String, String> transaction = new HashMap<>();

                        // adding each child node to HashMap key => value
                        transaction.put("agid", agid);
                        transaction.put("userid", userid);
                        transaction.put("amount", amount);

                        // adding contact to contact list
                        transactionList.add(transaction);
                    }
                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    TestActivity.this, transactionList,
                    R.layout.list_item, new String[]{"agid", "userid",
                    "amount"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
        }

    }
}
