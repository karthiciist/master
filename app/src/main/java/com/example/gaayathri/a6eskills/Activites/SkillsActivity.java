package com.example.gaayathri.a6eskills.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Fragments.SkillsViewFragment;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.SkillClickListoner;
import com.example.gaayathri.a6eskills.Skills;
import com.example.gaayathri.a6eskills.adapter.MainSkillAdoptor;
import com.example.gaayathri.a6eskills.adapter.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends AppCompatActivity implements SkillClickListoner {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Skills> selectedmainskills = new ArrayList<>();

    SkillClickListoner skillClickListoner = this;


    Skills skills_1;
    Skills skills_2;
    Skills skills_3;
    Skills skills_4;
    Skills skills_5;
    Skills skills_6;
    List<Skills> maininput;
Button skillnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_skills);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent homeintent = new Intent(SkillsActivity.this, UserMoreDataActivity.class);
            startActivity(homeintent);
            SkillsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });


        //Added
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        skillnext =(Button)findViewById(R.id.skillnext);

        skillnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //////Next Activity Method here
                Toast.makeText(SkillsActivity.this, getPostData(), Toast.LENGTH_SHORT).show();

            }
        });

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        maininput = new ArrayList<>();

        skills_1 = new Skills();
        skills_1.setId(1);
        skills_1.setName("Data Science");
        skills_1.setChild(new ArrayList<Skills>());

        maininput.add(skills_1);

        skills_2 = new Skills();
        skills_2.setId(12);
        skills_2.setName("Data Engineering");
        skills_2.setChild(new ArrayList<Skills>());

        maininput.add(skills_2);

        skills_3 = new Skills();
        skills_3.setId(25);
        skills_3.setName("Devops");
        skills_3.setChild(new ArrayList<Skills>());

        maininput.add(skills_3);

        skills_4 = new Skills();
        skills_4.setId(42);
        skills_4.setName("Analytics and Reporting");
        skills_4.setChild(new ArrayList<Skills>());

        maininput.add(skills_4);

        skills_5 = new Skills();
        skills_5.setId(49);
        skills_5.setName("Infrastructure/Security/Identity Management");
        skills_5.setChild(new ArrayList<Skills>());

        maininput.add(skills_5);

        skills_6 = new Skills();
        skills_6.setId(71);
        skills_6.setName("Automation");
        skills_6.setChild(new ArrayList<Skills>());

        maininput.add(skills_6);


        mAdapter = new MainSkillAdoptor(maininput, skillClickListoner);
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SkillsViewFragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void selectedSkill(Skills skills) {


        if ((getTotelSelectedSkills() == 4 || getTotelSelectedMainSkills() == 2) && skills.getChild().isEmpty()) {

            Toast.makeText(this, "You Can Only Select maximum of 4 SubSkills and 2 MainSkills..!", Toast.LENGTH_SHORT).show();

            return;
        }

        selectedmainskills.add(skills);

        try {
            // getSubList(skills);
            List<Skills> object = getSubList(skills);
            Intent intent = new Intent(SkillsActivity.this, SelectSkillsActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST", (Serializable) object);
            intent.putExtra("BUNDLE", args);
            intent.putExtra("PARENT", skills.getId());
            startActivity(intent);


        } catch (JSONException e) {

            e.printStackTrace();
        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        //  super.onNewIntent(intent);
        Bundle args = intent.getBundleExtra("SELECTEDBUNDLE");
        List<Skills> input = (ArrayList<Skills>) args.getSerializable("SELECTEDARRAYLIST");
        Integer PARENT = intent.getIntExtra("PARENT", 0);
        switch (PARENT) {

            case 1:

                skills_1.setChild(input);
                break;

            case 12:
                skills_2.setChild(input);


                break;

            case 25:
                skills_3.setChild(input);


                break;

            case 42:

                skills_4.setChild(input);

                break;

            case 49:

                skills_5.setChild(input);

                break;

            case 71:

                skills_6.setChild(input);

                break;

        }


        mAdapter = new MainSkillAdoptor(maininput, skillClickListoner);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deselectSkill(Skills skills) {

    }

    @Override
    public Integer selectedCount() {
        return null;
    }


    Integer getTotelSelectedSkills() {
        Integer count = 0;

        if (!skills_1.getChild().isEmpty()) {
            count = count + skills_1.getChild().size();
        }

        if (!skills_2.getChild().isEmpty()) {
            count = count + skills_2.getChild().size();
        }

        if (!skills_3.getChild().isEmpty()) {
            count = count + skills_3.getChild().size();
        }

        if (!skills_4.getChild().isEmpty()) {
            count = count + skills_4.getChild().size();
        }

        if (!skills_5.getChild().isEmpty()) {
            count = count + skills_5.getChild().size();
        }

        if (!skills_6.getChild().isEmpty()) {
            count = count + skills_6.getChild().size();
        }
        return count;
    }


    String getPostData() {

      String  postdata = "";
        Integer count = 0;

        if (!skills_1.getChild().isEmpty()) {

            List<Skills> skills = skills_1.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }


        }

        if (!skills_2.getChild().isEmpty()) {
            List<Skills> skills = skills_2.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }
        }

        if (!skills_3.getChild().isEmpty()) {
            List<Skills> skills = skills_3.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }
        }

        if (!skills_4.getChild().isEmpty()) {
            List<Skills> skills = skills_4.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }
        }

        if (!skills_5.getChild().isEmpty()) {
            List<Skills> skills = skills_5.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }
        }

        if (!skills_6.getChild().isEmpty()) {
            List<Skills> skills = skills_6.getChild();

            for (Skills skill :
                    skills) {

                postdata = postdata + "," + skill.getId();
            }
        }
      return postdata;
    }


    Integer getTotelSelectedMainSkills() {
        Integer count = 0;

        if (!skills_1.getChild().isEmpty()) {
            count = count + 1;
        }

        if (!skills_2.getChild().isEmpty()) {
            count = count + 1;
        }

        if (!skills_3.getChild().isEmpty()) {
            count = count + 1;
        }

        if (!skills_4.getChild().isEmpty()) {
            count = count + 1;
        }

        if (!skills_5.getChild().isEmpty()) {
            count = count + 1;

        }

        if (!skills_6.getChild().isEmpty()) {
            count = count + 1;
        }
        return count;
    }


    List<Skills> getSubList(Skills skills) throws JSONException {
        JSONArray array = new JSONArray("[ { 'CVM_SKILL_ID': '1', 'CVM_SKILL_NAME': 'Data Science', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '2', 'CVM_SKILL_NAME': 'Statistical Programming', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '3', 'CVM_SKILL_NAME': 'Languages and Tools', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '4', 'CVM_SKILL_NAME': 'Python', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '3', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '5', 'CVM_SKILL_NAME': 'R', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '3', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '6', 'CVM_SKILL_NAME': 'SAS', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '3', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '7', 'CVM_SKILL_NAME': 'SQL/Database', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '3', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '8', 'CVM_SKILL_NAME': 'Machine Learning Models', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '9', 'CVM_SKILL_NAME': 'Statistical Packages', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '10', 'CVM_SKILL_NAME': 'Deep Learning', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '11', 'CVM_SKILL_NAME': 'Complex and Unstructured Data, including Text', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '1', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '12', 'CVM_SKILL_NAME': 'Data Engineering', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '13', 'CVM_SKILL_NAME': 'Dimensional Modeling', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '14', 'CVM_SKILL_NAME': 'RedShift/AWS/Linux/Jenkins', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '24', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '15', 'CVM_SKILL_NAME': 'SQLServer/Azure', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '24', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '16', 'CVM_SKILL_NAME': 'Python', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '24', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '17', 'CVM_SKILL_NAME': 'API Creation to input/export data', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '18', 'CVM_SKILL_NAME': 'DevOps background', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '19', 'CVM_SKILL_NAME': 'Visualization Experience', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '20', 'CVM_SKILL_NAME': 'Data Warehousing ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '21', 'CVM_SKILL_NAME': 'Informatica', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '20', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '22', 'CVM_SKILL_NAME': 'Ab Initio', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '20', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '23', 'CVM_SKILL_NAME': 'Data Stage', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '20', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '24', 'CVM_SKILL_NAME': 'Platforms', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '12', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '25', 'CVM_SKILL_NAME': 'DevOps', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '26', 'CVM_SKILL_NAME': 'Java/ AWS', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '27', 'CVM_SKILL_NAME': '.NET /Azure', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '28', 'CVM_SKILL_NAME': 'Pivotal Cloud Foundry', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '29', 'CVM_SKILL_NAME': 'MEAN ( AngJs)', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '30', 'CVM_SKILL_NAME': 'MERN ( ReactJs)', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '31', 'CVM_SKILL_NAME': 'Testing', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '25', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '33', 'CVM_SKILL_NAME': 'IoT', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '34', 'CVM_SKILL_NAME': 'BlockChain', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '35', 'CVM_SKILL_NAME': 'RPA using ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '36', 'CVM_SKILL_NAME': 'UIPath', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '35', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '37', 'CVM_SKILL_NAME': 'BluePrism', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '35', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '38', 'CVM_SKILL_NAME': 'AutomationAnywhere', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '35', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '39', 'CVM_SKILL_NAME': 'Artificial Intelligence', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '40', 'CVM_SKILL_NAME': 'Machine Learning', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '41', 'CVM_SKILL_NAME': 'Connected Car Technologies', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '71', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '42', 'CVM_SKILL_NAME': 'Analytics and Reporting', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '43', 'CVM_SKILL_NAME': 'Oracle BI', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '44', 'CVM_SKILL_NAME': 'SAP BI', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '45', 'CVM_SKILL_NAME': 'Tableau', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '46', 'CVM_SKILL_NAME': 'Qlikview', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '47', 'CVM_SKILL_NAME': 'R', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '48', 'CVM_SKILL_NAME': 'Microsoft SSIS/SSAS/SSRS', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '42', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '49', 'CVM_SKILL_NAME': 'Infrastructure/Security/Identity Management', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '50', 'CVM_SKILL_NAME': 'Sys Admin ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '51', 'CVM_SKILL_NAME': 'Linux', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '50', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '52', 'CVM_SKILL_NAME': 'Windows', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '50', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '53', 'CVM_SKILL_NAME': 'Database Admin ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '54', 'CVM_SKILL_NAME': 'Oracle', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '53', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '55', 'CVM_SKILL_NAME': 'DB/2', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '53', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '56', 'CVM_SKILL_NAME': 'MySQL', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '53', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '57', 'CVM_SKILL_NAME': 'SQLServer', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '53', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '58', 'CVM_SKILL_NAME': 'AppServer Admin ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '59', 'CVM_SKILL_NAME': 'Oracle Weblogic', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '58', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '60', 'CVM_SKILL_NAME': 'Websphere', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '58', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '61', 'CVM_SKILL_NAME': 'JBOSS', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '58', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '62', 'CVM_SKILL_NAME': 'Firewalls, Vulnerability, Web Monitoring', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '63', 'CVM_SKILL_NAME': 'Digital Identity Management ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '64', 'CVM_SKILL_NAME': 'ForgeRock', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '63', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '65', 'CVM_SKILL_NAME': 'Oracle', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '63', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '66', 'CVM_SKILL_NAME': 'Ping Identity', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '63', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '67', 'CVM_SKILL_NAME': 'Digital Rights Management ', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '49', 'CVM_SKILL_CAN_SLT_YN': 'Y' }, { 'CVM_SKILL_ID': '68', 'CVM_SKILL_NAME': 'Amazon DRM', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '67', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '69', 'CVM_SKILL_NAME': 'Apple Fairplay', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '67', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '70', 'CVM_SKILL_NAME': 'Adobe ADEPT', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': '67', 'CVM_SKILL_CAN_SLT_YN': null }, { 'CVM_SKILL_ID': '71', 'CVM_SKILL_NAME': 'Automation', 'CVM_SKILL_DESC': null, 'CVM_SKILL_STATUS_YN': 'Y', 'CVM_SKILL_PT_ID': null, 'CVM_SKILL_CAN_SLT_YN': null } ] ");


        List<Skills> skilllist = new ArrayList<>();


        for (int i = 0; i < array.length(); i++) {

            JSONObject jsonObject = array.getJSONObject(i);


            try {
                if (jsonObject.getString("CVM_SKILL_PT_ID").equals(String.valueOf(skills.getId()))) {


                    Skills obj = new Skills();

                    obj.setName(jsonObject.optString("CVM_SKILL_NAME"));
                    obj.setId(Integer.parseInt(jsonObject.optString("CVM_SKILL_ID")));
                    obj.setSelectable(false);
                    if (jsonObject.optString("CVM_SKILL_CAN_SLT_YN").equals("Y")) {
                        obj.setSelectable(true);
                    }
                    skilllist.add(obj);


                    List<Skills> lastchildlist = new ArrayList<>();

                    for (int j = 0; j < array.length(); j++) {

                        JSONObject jsonObjectChild = array.getJSONObject(j);


                        if (jsonObjectChild.getString("CVM_SKILL_PT_ID").equals(String.valueOf(obj.getId()))) {

                            Skills objlc = new Skills();

                            objlc.setName(jsonObjectChild.optString("CVM_SKILL_NAME"));
                            objlc.setId(Integer.parseInt(jsonObjectChild.optString("CVM_SKILL_ID")));
                            objlc.setSelectable(false);
                            if (jsonObjectChild.optString("CVM_SKILL_CAN_SLT_YN").equals("Y")) {
                                objlc.setSelectable(true);
                            }
                            lastchildlist.add(objlc);
                            /////////////////////////
                            //////////////////////////
                            skilllist.add(objlc);
                            /////////////////////////
                            //////////////////////////
                        }
                        obj.setChild(lastchildlist);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return skilllist;
    }


}