package com.example.gaayathri.a6eskills.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.SkillClickListoner;
import com.example.gaayathri.a6eskills.Skills;
import com.example.gaayathri.a6eskills.adapter.MainSkillAdoptor;
import com.example.gaayathri.a6eskills.adapter.SubSkillAdoptor;

import java.util.ArrayList;
import java.util.List;

public class SelectSkillsActivity extends AppCompatActivity implements  SkillClickListoner {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Skills> selectedmainskills = new ArrayList<>();

    SkillClickListoner skillClickListoner = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_skills);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        List<Skills> input = (ArrayList<Skills>) args.getSerializable("ARRAYLIST");

        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent homeintent = new Intent(SelectSkillsActivity.this, SkillsActivity.class);
            startActivity(homeintent);
        });


        //Addded
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SubSkillAdoptor(input, skillClickListoner);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public Skills selectedSkill(Skills skills) {

        return null;
    }
}
