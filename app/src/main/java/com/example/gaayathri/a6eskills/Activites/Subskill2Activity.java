package com.example.gaayathri.a6eskills.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.AnalyticsSubskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.AutomationSubskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.DataEngineeringSunskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.DataScienceSubskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.DevopsSubskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments.InfraSubskills2Fragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.AnalyticsSubskillsFragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.AutomationSubskillsFragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.DataEngineeringSunskillsFragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.DataScienceSubskillsFragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.DevopsSubskillsFragment;
import com.example.gaayathri.a6eskills.Fragments.SubskillFragments.InfraSubskillsFragment;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.adapter.ViewPagerAdapter;

public class Subskill2Activity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subskill2);

        sharedpreferences = Subskill2Activity.this.getSharedPreferences("mypref", 0); // 0 - for private mode
        //String skilll = sharedpreferences.getString("skill1", "....");
        String mainSkill2 = sharedpreferences.getString("mainSkill2", "....");

        //Toast.makeText(this, skilll + "+" + skill2, Toast.LENGTH_SHORT).show();

        String title = "Please choose your expertise in the field of " + mainSkill2;
        TextView titleview = findViewById(R.id.title);
        titleview.setText(title);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        if (mainSkill2.equals("DataScience")){
            setupViewPager(viewPager);
        } else if (mainSkill2.equals("Data Engineering")){
            setupViewPager1(viewPager);
        } else if (mainSkill2.equals("DevOps")){
            setupViewPager2(viewPager);
        } else if (mainSkill2.equals("Automation")){
            setupViewPager3(viewPager);
        } else if (mainSkill2.equals("Analytics and Reporting")){
            setupViewPager4(viewPager);
        } else if (mainSkill2.equals("Infrastructure/Security/Identity Management")){
            setupViewPager5(viewPager);
        }

        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent homeintent = new Intent(Subskill2Activity.this, UserMoreDataActivity.class);
            startActivity(homeintent);
            Subskill2Activity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DataScienceSubskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager1(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DataEngineeringSunskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager2(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DevopsSubskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager3(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AutomationSubskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager4(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AnalyticsSubskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager5(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new InfraSubskills2Fragment(), "SkillsView");
        viewPager.setAdapter(adapter);
    }
}
