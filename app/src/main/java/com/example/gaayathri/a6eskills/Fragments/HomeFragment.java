package com.example.gaayathri.a6eskills.Fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gaayathri.a6eskills.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class HomeFragment extends Fragment {

    Dialog scheduleDialod;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";


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

        Button btnView1 = view.findViewById(R.id.btnView1);
        btnView1.setOnClickListener(v -> {
            scheduleDialod.show();

            Button btnDecline = scheduleDialod.findViewById(R.id.btnDecline);
            btnDecline.setOnClickListener(v1 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview declined", Toast.LENGTH_SHORT).show();
            });

            Button btnAccept = scheduleDialod.findViewById(R.id.btnAccept);
            btnAccept.setOnClickListener(v12 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview accepted", Toast.LENGTH_SHORT).show();
            });

            Button btnPostpone = scheduleDialod.findViewById(R.id.btnPostpone);
            btnPostpone.setOnClickListener(v13 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview postponed", Toast.LENGTH_SHORT).show();
            });
        });

        Button btnView2 = view.findViewById(R.id.btnView2);
        btnView2.setOnClickListener(v -> {
            scheduleDialod.show();

            Button btnDecline = scheduleDialod.findViewById(R.id.btnDecline);
            btnDecline.setOnClickListener(v14 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview declined", Toast.LENGTH_SHORT).show();
            });

            Button btnAccept = scheduleDialod.findViewById(R.id.btnAccept);
            btnAccept.setOnClickListener(v15 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview accepted", Toast.LENGTH_SHORT).show();
            });
        });

        Button btnView3 = view.findViewById(R.id.btnView3);
        btnView3.setOnClickListener(v -> {
            scheduleDialod.show();

            Button btnDecline = scheduleDialod.findViewById(R.id.btnDecline);
            btnDecline.setOnClickListener(v16 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview declined", Toast.LENGTH_SHORT).show();
            });

            Button btnAccept = scheduleDialod.findViewById(R.id.btnAccept);
            btnAccept.setOnClickListener(v17 -> {
                scheduleDialod.dismiss();
                Toast.makeText(getActivity(), "Interview accepted", Toast.LENGTH_SHORT).show();
            });
        });

        return view;
    }

}
