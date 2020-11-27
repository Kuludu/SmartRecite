package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class HomeActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private StudyFragment studyFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        studyFragment = new StudyFragment();
        settingFragment = new SettingFragment();
        setContentView(R.layout.activity_home);
        review(getWindow().getDecorView());
    }

    public void setStudyFragment(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    public void setting(View v) {
        setStudyFragment(settingFragment);
    }

    public void review(View v) {
        setStudyFragment(studyFragment);
    }

    public void wrong(View v) {
        Intent intent = new Intent(this, WrongActivity.class);
        startActivity(intent);
    }
}