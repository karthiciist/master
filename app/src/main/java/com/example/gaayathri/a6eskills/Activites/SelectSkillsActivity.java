package com.example.gaayathri.a6eskills.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.SkillClickListoner;
import com.example.gaayathri.a6eskills.Skills;
import com.example.gaayathri.a6eskills.adapter.SubSkillAdoptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectSkillsActivity extends AppCompatActivity implements SkillClickListoner {

private TextView ok;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Skills> selectedmainskills = new ArrayList<>();

    SkillClickListoner skillClickListoner = this;

    Integer PARENT;

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
 PARENT = intent.getIntExtra("PARENT",0);


        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent homeintent = new Intent(SelectSkillsActivity.this, SkillsActivity.class);
            startActivity(homeintent);
            SelectSkillsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });

ok=(TextView)findViewById(R.id.tv_ok);
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


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(SelectSkillsActivity.this, SkillsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle args = new Bundle();
                args.putSerializable("SELECTEDARRAYLIST",(Serializable)selectedmainskills);
                intent.putExtra("SELECTEDBUNDLE",args);
                intent.putExtra("PARENT",PARENT);
                startActivityIfNeeded(intent, 0);
                SelectSkillsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);


            }
        });

    }


    @Override
    public void selectedSkill(Skills skills) {



        selectedmainskills.add(skills);

    }

    @Override
    public void deselectSkill(Skills skills) {

        List<Skills> selectedmainskillsupdated = new ArrayList<>();
        for (Skills skill : selectedmainskills) {
            if (skills.getId() != skill.getId()) {
                selectedmainskillsupdated.add(skill);
            }
        }
        selectedmainskills = selectedmainskillsupdated;
    }

    @Override
    public Integer selectedCount() {
        if(selectedmainskills.size() == 2){
            Toast.makeText(this, "You Can Select Maximum of 2 Skills..!", Toast.LENGTH_SHORT).show();
        }
        return selectedmainskills.size();
    }
}
