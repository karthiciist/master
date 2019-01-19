package com.example.gaayathri.a6eskills.Activites;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.gaayathri.a6eskills.Fragments.LevelViewFragment;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.adapter.ViewPagerAdapter;

public class LevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_level);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent homeintent = new Intent(LevelActivity.this, SkillsActivity.class);
            startActivity(homeintent);
            LevelActivity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LevelViewFragment(), "LevelView");
        viewPager.setAdapter(adapter);
    }
}
