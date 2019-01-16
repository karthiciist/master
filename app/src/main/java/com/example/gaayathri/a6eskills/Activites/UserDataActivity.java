package com.example.gaayathri.a6eskills.Activites;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Utils.PinEntryEditText;
import com.example.gaayathri.a6eskills.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class UserDataActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;

    String name, email, password, phoneNo, whatsappFlag;

    int countryCode;

    public static final int MY_REQUEST_READ_GALLERY   = 13;
    public static final int MY_REQUEST_WRITE_GALLERY   = 14;
    public static final int MY_REQUEST_GALLERY   = 15;

    TextView verify;
    String verified;

    Uri uriObject;
    Uri uriObjectIntent;

    public File filen = null;

    Dialog phoneVerificationDialog;

    CircularImageView profilePic;
    StorageReference mStorageReference;

    String profilePicDownloadUrl;

    CountryCodePicker ccp;
    CountryCodePicker ccpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_data);

        EditText et_name = findViewById(R.id.et_name);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_password = findViewById(R.id.et_password);
        EditText et_phone = findViewById(R.id.et_phone);
        //EditText et_contryCode = findViewById(R.id.et_contryCode);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        String code = ccp.getSelectedCountryCode();

        StartFirebaseLogin();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        Button btnNext = findViewById(R.id.btnNext);
        verify = findViewById(R.id.verify);
        //verify.setText("Verified");

        profilePic = findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionRG();
            }
        });

        sharedpreferences = UserDataActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
        verified = sharedpreferences.getString("verified", "0");
        name = sharedpreferences.getString("name", "");
        email = sharedpreferences.getString("email", "");
        password = sharedpreferences.getString("password", "");
        phoneNo = sharedpreferences.getString("phoneNo", "");
        countryCode = sharedpreferences.getInt("countryCode", 91);


        et_name.setText(name);
        et_email.setText(email);
        et_password.setText(password);
        et_phone.setText(phoneNo);
        ccp.setCountryForPhoneCode(countryCode);

        if (verified == "1") {

            verify.setText("Verified");
            verify.setTextColor(getResources().getColor(R.color.green_400));

        } else {

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    phoneVerificationDialog = new Dialog(UserDataActivity.this);
                    phoneVerificationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    phoneVerificationDialog.setContentView(R.layout.dialog_phone_verification);

                    LinearLayout otpET = phoneVerificationDialog.findViewById(R.id.otpET);
                    otpET.setVisibility(View.GONE);

                    //EditText et_phonenoEditText = phoneVerificationDialog.findViewById(R.id.et_phoneno);


                    phoneVerificationDialog.show();

                    Button btnVerify = phoneVerificationDialog.findViewById(R.id.btnVerify);
                    Button btnResend = phoneVerificationDialog.findViewById(R.id.btnResend);
                    Button btnBack = phoneVerificationDialog.findViewById(R.id.btnBack);
                    ccpDialog = phoneVerificationDialog.findViewById(R.id.ccpDialog);
                    ccpDialog.setCountryForPhoneCode(ccp.getSelectedCountryCodeAsInt());

                    btnVerify.setVisibility(View.GONE);
                    btnResend.setVisibility(View.GONE);
                    btnBack.setVisibility(View.GONE);

                    Button btnSend = phoneVerificationDialog.findViewById(R.id.btnSend);

                    EditText et_phoneno = phoneVerificationDialog.findViewById(R.id.et_phoneno);

                    et_phoneno.setText(et_phone.getText().toString());

                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(UserDataActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            otpET.setVisibility(View.VISIBLE);

                            int countryCode = ccpDialog.getSelectedCountryCodeAsInt();
                            String phoneNumber = et_phoneno.getText().toString();

                            String fullNumber = countryCode + phoneNumber;

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    fullNumber,                     // Phone number to verify
                                    60,                           // Timeout duration
                                    TimeUnit.SECONDS,                // Unit of timeout
                                    UserDataActivity.this,        // Activity (for callback binding)
                                    mCallback);                      // OnVerificationStateChangedCallbacks

                            btnVerify.setVisibility(View.VISIBLE);
                            btnResend.setVisibility(View.VISIBLE);

                            btnSend.setVisibility(View.GONE);
                        }
                    });

                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PinEntryEditText txt_pin_entry = phoneVerificationDialog.findViewById(R.id.txt_pin_entry);
                            String Otp = txt_pin_entry.getText().toString();

                            try {
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, Otp);
                                SigninWithPhone(credential);
                            } catch (Exception e) {
                                Toast.makeText(UserDataActivity.this, "Please Check The OTP Entered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnResend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(UserDataActivity.this, "Please Try After 60 Seconds", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }

        btnNext.setOnClickListener(v -> {

            String key = verify.getText().toString();

            String nameSP = et_name.getText().toString();
            String emailSP = et_email.getText().toString();
            String passwordSP = et_password.getText().toString();
            String phoneNoSP = et_phone.getText().toString();

           // if (!(key == "Verified")) {
                if ((key == "Verified")) {
                Toast.makeText(this, "Verify Your Mobile Number", Toast.LENGTH_SHORT).show();
            } else if ((nameSP.equals("")) || (emailSP.equals("")) || (passwordSP.equals("")) || (phoneNoSP.equals(""))) {
                Toast.makeText(this, "Kindly Fill All Fields..!", Toast.LENGTH_SHORT).show();
            } else {
                int countryCodeSP = ccp.getSelectedCountryCodeAsInt();

                CheckBox whatsapp = findViewById(R.id.whatsappFlag);

                if (whatsapp.isChecked()) {
                    whatsappFlag = "Y";
                } else {
                    whatsappFlag = "N";
                }

                sharedpreferences = UserDataActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("name", nameSP);
                editor.putString("email", emailSP);
                editor.putString("password", passwordSP);
                editor.putString("phoneNo", phoneNoSP);
                editor.putString("whatsappFlag", whatsappFlag);
                editor.putInt("countryCode", countryCodeSP);

                editor.apply();

                Intent homeintent = new Intent(UserDataActivity.this, UserMoreDataActivity.class);
                startActivity(homeintent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        });

    }

    private void checkPermissionRG() {

        int permissionCheck = ContextCompat.checkSelfPermission(UserDataActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    UserDataActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ_GALLERY);
        } else {
            checkPermissionWG();
        }

    }

    private void checkPermissionWG(){
        int permissionCheck = ContextCompat.checkSelfPermission(UserDataActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    UserDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE_GALLERY);
        } else {
            getPhotos();
        }
    }

    private void getPhotos() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, MY_REQUEST_GALLERY);
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions,  int[] grantResults)
    {

        switch (requestCode) {

            case MY_REQUEST_READ_GALLERY:
                checkPermissionWG();
                break;
            case MY_REQUEST_WRITE_GALLERY:
                getPhotos();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {

            case MY_REQUEST_GALLERY:
                try {

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    filen = getFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(filen);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();

                    profilePic.setImageURI(Uri.parse("file:///" + filen));//fresco library

                    uriObject = Uri.parse("file:///" + filen);

                } catch (Exception e) {

                    Log.e("", "Error while creating temp file", e);
                }
                break;

        }
    }

    public File getFile(){

        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                return null;
            }
        }

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        File mediaFile = new File(fileDir.getPath() + File.separator + ts + ".jpg");
        return mediaFile;
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //startActivity(new Intent(VerificationActivity.this,SignedIn.class));
                            //finish();
                            Toast.makeText(UserDataActivity.this,"Verification successful",Toast.LENGTH_SHORT).show();

                            ImageView iv = phoneVerificationDialog.findViewById(R.id.iv);
                            iv.setImageDrawable(getDrawable(R.drawable.tick));

                            Button btnBack = phoneVerificationDialog.findViewById(R.id.btnBack);
                            btnBack.setVisibility(View.VISIBLE);

                            sharedpreferences = UserDataActivity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("verified", "1");
                            editor.apply();

                            btnBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseAuth.getInstance().signOut();
                                    phoneVerificationDialog.dismiss();
                                }
                            });

                            Button btnVerify = phoneVerificationDialog.findViewById(R.id.btnVerify);
                            Button btnResend = phoneVerificationDialog.findViewById(R.id.btnResend);

                            btnVerify.setVisibility(View.GONE);
                            btnResend.setVisibility(View.GONE);

                            verify.setText("Verified");
                            verify.setTextColor(getResources().getColor(R.color.green_400));

                        } else {
                            Toast.makeText(UserDataActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(UserDataActivity.this,"verification completed",Toast.LENGTH_SHORT).show();

                ImageView iv = phoneVerificationDialog.findViewById(R.id.iv);
                iv.setImageDrawable(getDrawable(R.drawable.tick));

                LinearLayout otpET = phoneVerificationDialog.findViewById(R.id.otpET);
                otpET.setVisibility(View.GONE);

                Button btnVerify = phoneVerificationDialog.findViewById(R.id.btnVerify);
                Button btnResend = phoneVerificationDialog.findViewById(R.id.btnResend);

                btnVerify.setVisibility(View.GONE);
                btnResend.setVisibility(View.GONE);

                verify.setText("Verified");
                verify.setTextColor(getResources().getColor(R.color.green_400));

                Button btnBack = phoneVerificationDialog.findViewById(R.id.btnBack);
                btnBack.setVisibility(View.VISIBLE);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        phoneVerificationDialog.dismiss();
                    }
                });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(UserDataActivity.this,"verification failed: " + e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(UserDataActivity.this,"OTP Sent..!",Toast.LENGTH_SHORT).show();
            }
        };
    }

}
