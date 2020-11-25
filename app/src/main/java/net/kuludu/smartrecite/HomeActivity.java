package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.home_activity);
    }

    public void setting(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void review(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void wrong(View v) {
        Intent intent = new Intent(this, WrongActivity.class);
        startActivity(intent);
    }
}