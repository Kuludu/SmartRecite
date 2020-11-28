package net.kuludu.smartrecite;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private StudyFragment studyFragment;
    private SettingFragment settingFragment;
    private ScreenListener screenListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        initControl();
        review(getWindow().getDecorView());
    }

    private void initControl() {
        studyFragment = new StudyFragment();
        settingFragment = new SettingFragment();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenStateListener() {
            @Override
            public void onScreenOn() {
                if (sharedPreferences.getBoolean("btnTf", false)) {
                    if (sharedPreferences.getBoolean("tf", false)) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onScreenOff() {
                editor.putBoolean("tf", true);
                editor.apply();
            }

            @Override
            public void onUnLock() {
                editor.putBoolean("tf", false);
                editor.apply();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenListener.unregisterListener();
    }

    public void setStudyFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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

    public void reviewWord(View v) {
        Intent intent = new Intent(this, RightActivity.class);
        startActivity(intent);
    }
}