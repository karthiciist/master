package com.example.gaayathri.a6eskills.Activites;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;

public class RedeemActivity extends AppCompatActivity {

    Dialog redeemDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        Window window = RedeemActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(RedeemActivity.this,R.color.colorPrimary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        redeemDialog = new Dialog(RedeemActivity.this);
        redeemDialog.setContentView(R.layout.dialog_redeem);

        TextView earnedPoints = findViewById(R.id.earnedPoints);
        int totalEarnedPoints = Integer.valueOf(earnedPoints.getText().toString());

        Button btnNewRequest = findViewById(R.id.btnNewRequest);
        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemDialog.show();
            }
        });

        Button btnRedeem = redeemDialog.findViewById(R.id.btnRedeem);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_pointsToRedeem = redeemDialog.findViewById(R.id.et_pointsToRedeem);
                int pointsToRedeem = Integer.valueOf(et_pointsToRedeem.getText().toString());

                if (pointsToRedeem < totalEarnedPoints) {
                    Toast.makeText(RedeemActivity.this, "Requested", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RedeemActivity.this, "Requested amount cannot be greater than Earned Points", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCancel = redeemDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemDialog.dismiss();
            }
        });

    }
}
