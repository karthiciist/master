package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;
import com.github.loadingview.LoadingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UserMoreDataActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    String name, email, city, phoneNo;

    String company, blackCompanies;

    ProgressBar progressBar;
    LinearLayout resumeUploadedLL;

    String downloadUrl;

    Uri uri;

    public static final int MY_REQUEST_CAMERA   = 10;
    public static final int MY_REQUEST_WRITE_CAMERA   = 11;
    public static final int CAPTURE_CAMERA   = 12;

    public static final int MY_REQUEST_READ_GALLERY   = 13;
    public static final int MY_REQUEST_WRITE_GALLERY   = 14;
    public static final int MY_REQUEST_GALLERY   = 15;


    //this is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    //the firebase objects for storage and database
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_more_data);

        sharedpreferences = UserMoreDataActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
        name = sharedpreferences.getString("name", "interviewer" + System.currentTimeMillis());
        email = sharedpreferences.getString("email", "");

        //getting firebase objects
        mStorageReference = FirebaseStorage.getInstance().getReference();

        Button btnNext = findViewById(R.id.btnNext);
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnResume = findViewById(R.id.btnResume);

        EditText et_company = findViewById(R.id.et_company);
        EditText et_blackcompanies = findViewById(R.id.et_blackcompanies);
        EditText et_city = findViewById(R.id.et_city);

        btnResume.setOnClickListener(this);

        btnNext.setOnClickListener(v -> {

            company = et_company.getText().toString();
            blackCompanies = et_blackcompanies.getText().toString();
            city = et_city.getText().toString();

            if ((company.length() != 0) & (city.length() != 0)){

                sharedpreferences = UserMoreDataActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("company", company);
                editor.putString("blackCompanies", blackCompanies);
                editor.putString("resumeDownloadUrl", downloadUrl);
                editor.putString("city", city);
                editor.apply();
                if (uri != null) {
                    uploadFile(uri);
                }
                Intent homeintent = new Intent(UserMoreDataActivity.this, SkillsActivity.class);
                startActivity(homeintent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            } else {
                Toast.makeText(this, "Please Fill All Fields..!", Toast.LENGTH_SHORT).show();
            }


        });

        btnBack.setOnClickListener(v -> {
            Intent homeintent = new Intent(UserMoreDataActivity.this, UserDataActivity.class);
            startActivity(homeintent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });

    }

    private void getPDF() {

        int permissionCheck = ContextCompat.checkSelfPermission(UserMoreDataActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    UserMoreDataActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CAMERA);
            getPDF();
        } else {
            //creating an intent for file chooser
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Resume"), PICK_PDF_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file

                uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String displayName = uri2filename();
                TextView resumeName = findViewById(R.id.resumeName);
                resumeName.setText(displayName);

                loadingDialog = new Dialog(this);
                loadingDialog.setContentView(R.layout.dialog_loading);
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#801b5e20")));
                LoadingView loadingView = loadingDialog.findViewById(R.id.loadingView);
                loadingView.start();
                loadingDialog.show();

                uploadFile(uri);


                //uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String uri2filename() {

        String ret = null;
        String scheme = uri.getScheme();

        if (scheme.equals("file")) {
            ret = uri.getLastPathSegment();
        }
        else if (scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return ret;
    }

    private void uploadFile(Uri data) {
        StorageReference sRef = mStorageReference.child("uploads/" + email + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(UserMoreDataActivity.this, "Resume Uploading Done..!", Toast.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserMoreDataActivity.this," Resume Failed To Upload..!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnResume:
                getPDF();
                break;
        }
    }
}
