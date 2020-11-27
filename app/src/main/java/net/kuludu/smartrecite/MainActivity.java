package net.kuludu.smartrecite;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView timeText, dateText, wordText, englishText;
    private ImageView playVioce;
    private String mMonth, mDay, mWeek, mHours, mMinute;
    private RadioGroup radioGroup;
    private RadioButton radioOne, radioTwo, radioThree;
    private WordHelper wordHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LinearLayout linearLayout;
    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;

    int id;
    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initDatabaseHelper();
        initControl();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // get system calendar
        Calendar calendar = Calendar.getInstance();
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));


        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            mHours = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            mHours = "" + calendar.get(Calendar.HOUR_OF_DAY);
        }

        if (calendar.get(Calendar.MINUTE) < 10) {
            mMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            mMinute = "" + calendar.get(Calendar.MINUTE);
        }

        // get week and show
        if ("1".equals(mWeek)) {
            mWeek = "天";
        } else if ("2".equals(mWeek)) {
            mWeek = "一";
        } else if ("3".equals(mWeek)) {
            mWeek = "二";
        } else if ("4".equals(mWeek)) {
            mWeek = "三";
        } else if ("5".equals(mWeek)) {
            mWeek = "四";
        } else if ("6".equals(mWeek)) {
            mWeek = "五";
        } else if ("7".equals(mWeek)) {
            mWeek = "六";
        }
        timeText.setText(mHours + ":" + mMinute);
        dateText.setText(mMonth + "月" + mDay + "日" + "   " + "星期" + mWeek);
    }

    private void btnGetText(String msg, RadioButton btn) {
        Word word = wordHelper.getXWord(id);
        String right_chinese = word.getChinese();
        if (msg.equals(right_chinese)) {
            wordText.setTextColor(Color.GREEN);
            englishText.setTextColor(Color.GREEN);
            btn.setTextColor(Color.GREEN);
            saveRight(word);
        } else {
            wordText.setTextColor(Color.RED);
            englishText.setTextColor(Color.RED);
            btn.setTextColor(Color.RED);
            saveWrong(word);
        }
    }

    private void saveWrong(Word word) {
        Set<String> wrong = sharedPreferences.getStringSet("wrong", new HashSet<>());
        wrong.add(word.getIndex().toString());
        editor.putStringSet("wrong", wrong);
        editor.apply();
    }

    private void saveRight(Word word) {
        Set<String> wrong = sharedPreferences.getStringSet("right", new HashSet<>());
        wrong.add(word.getIndex().toString());
        editor.putStringSet("right", wrong);
        editor.apply();
    }

    private int getNextWord() {
        initTextColor();
        Word nextWord;
        Word prevWord;

        List<Word> words = wordHelper.getRandXWords(1);
        Word w = words.get(0);
        wordText.setText(w.getWord());
        englishText.setText(w.getSoundmark());
        id = w.getIndex();

        if (id == 0) {
            nextWord = wordHelper.getXWord(id + 1);
            prevWord = wordHelper.getXWord(id + 2);
        } else if (id == wordHelper.getWordsCount() - 1) {
            nextWord = wordHelper.getXWord(id - 1);
            prevWord = wordHelper.getXWord(id - 2);
        } else {
            nextWord = wordHelper.getXWord(id + 1);
            prevWord = wordHelper.getXWord(id - 1);
        }


        Random r = new Random();
        int random = r.nextInt(3);
        if (random == 0) {
            radioOne.setText("A:" + w.getChinese());
            radioTwo.setText("B:" + nextWord.getChinese());
            radioThree.setText("C:" + prevWord.getChinese());
        } else if (random == 1) {
            radioOne.setText("A:" + nextWord.getChinese());
            radioTwo.setText("B:" + w.getChinese());
            radioThree.setText("C:" + prevWord.getChinese());
        } else if (random == 2) {
            radioOne.setText("A:" + nextWord.getChinese());
            radioTwo.setText("B:" + prevWord.getChinese());
            radioThree.setText("C:" + w.getChinese());
        }
        return id;
    }

    private void initDatabaseHelper() {
        wordHelper = new WordHelper(this);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void initControl() {
        timeText = findViewById(R.id.time_text);
        dateText = findViewById(R.id.date_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        playVioce = findViewById(R.id.play_voice);
        radioGroup = findViewById(R.id.choose_group);
        radioOne = findViewById(R.id.choose_btn_one);
        radioTwo = findViewById(R.id.choose_btn_two);
        radioThree = findViewById(R.id.choose_btn_three);
        linearLayout = findViewById(R.id.background);
        radioGroup.setOnCheckedChangeListener(this);
        playVioce.setOnClickListener(this);

        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unlock");

        getNextWord();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_voice:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioGroup.setClickable(false);
        radioOne.setClickable(false);
        radioTwo.setClickable(false);
        radioThree.setClickable(false);
        switch (checkedId) {
            case R.id.choose_btn_one:
                String msg1 = radioOne.getText().toString().substring(2).trim();
                btnGetText(msg1, radioOne);
                break;
            case R.id.choose_btn_two:
                String msg2 = radioTwo.getText().toString().substring(2).trim();
                btnGetText(msg2, radioTwo);
                break;
            case R.id.choose_btn_three:
                String msg3 = radioThree.getText().toString().substring(2).trim();
                btnGetText(msg3, radioThree);
                break;
        }
    }

    private void initTextColor() {
        radioOne.setChecked(false);
        radioTwo.setChecked(false);
        radioThree.setChecked(false);
        radioOne.setClickable(true);
        radioTwo.setClickable(true);
        radioThree.setClickable(true);
        radioGroup.setClickable(true);

        radioOne.setTextColor(Color.WHITE);
        radioTwo.setTextColor(Color.WHITE);
        radioThree.setTextColor(Color.WHITE);
        wordText.setTextColor(Color.WHITE);
        englishText.setTextColor(Color.WHITE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            if (x2 - x1 > 200) {
                Toast.makeText(this, getString(R.string.mastered), Toast.LENGTH_SHORT).show();
                getNextWord();
            } else if (x1 - x2 > 200) {
                Toast.makeText(this, getString(R.string.unlocked), Toast.LENGTH_SHORT).show();
                unlock();
            } else if (y1 - y2 > 200) {
                changeBackground();
            }
        }
        return super.onTouchEvent(event);
    }

    private void unlock() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        kl.disableKeyguard();
        finish();
    }

    private void changeBackground() {
        Random random = new Random();
        int index = random.nextInt(5);
        List<Integer> backgrounds = new ArrayList<Integer>();
        backgrounds.add(R.mipmap.background_1);
        backgrounds.add(R.mipmap.background_2);
        backgrounds.add(R.mipmap.background_3);
        backgrounds.add(R.mipmap.background_4);
        backgrounds.add(R.mipmap.background_5);
        linearLayout.setBackgroundResource(backgrounds.get(index));
    }
}
