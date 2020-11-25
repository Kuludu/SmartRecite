package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView timeText, dateText, wordText, englishText;
    private ImageView playVioce;
    private String mMonth, mDay, mWeek, mHours, mMinute;
    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;
    private RadioGroup radioGroup;
    private RadioButton radioOne, radioTwo, radioThree;
    private SharedPreferences sharedPreferences;
    private WordHelper wordHelper;
    private QuoteHelper quoteHelper;
    SharedPreferences.Editor editor = null;

    int j = 0;
    List<Integer> list;

    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordHelper = new WordHelper(this);
        if (!wordHelper.isDatabaseExists()) {
            Toast.makeText(this, getString(R.string.wait_for_db_download), Toast.LENGTH_SHORT).show();
            finish();
        }
        quoteHelper = new QuoteHelper(this);
        if (!quoteHelper.isQuoteExists()) {
            Toast.makeText(this, getString(R.string.wait_for_quote_download), Toast.LENGTH_SHORT).show();
            finish();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        init_control();
    }

    public void init_control(){
        timeText = findViewById(R.id.time_text);
        dateText = findViewById(R.id.date_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        playVioce = findViewById(R.id.play_vioce);
        radioOne = findViewById(R.id.choose_btn_one);
        radioTwo = findViewById(R.id.choose_btn_two);
        radioThree = findViewById(R.id.choose_btn_three);
        radioGroup = findViewById(R.id.choose_group);
        radioGroup.setOnCheckedChangeListener(this);
        playVioce.setOnClickListener(this);
    }

    public void init() {
        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        list = new ArrayList<Integer>();
        Random r = new Random();
        while (list.size() < 10) {
            int i = r.nextInt();
            if (!list.contains(i)) {
                list.add(i);
            }
        }
        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unlock");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_vioce:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
