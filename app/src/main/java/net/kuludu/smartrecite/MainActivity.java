package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);


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

    @Override
    protected void onStart() {
        super.onStart();
        // get system calendar
        Calendar calendar = Calendar.getInstance();
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) +1);
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));


        if(calendar.get(Calendar.HOUR_OF_DAY) < 10){
            mHours = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        }else{
            mHours = "" + calendar.get(Calendar.HOUR_OF_DAY);
        }

        if(calendar.get(Calendar.MINUTE) < 10){
            mMinute = "0" + calendar.get(Calendar.MINUTE);
        }else{
            mMinute = "" + calendar.get(Calendar.MINUTE);
        }

        // get week and show
        if("1".equals(mWeek)){
            mWeek = "天";
        }else if("2".equals(mWeek)){
            mWeek = "一";
        }else if("3".equals(mWeek)){
            mWeek = "二";
        }else if("4".equals(mWeek)){
            mWeek = "三";
        }else if("5".equals(mWeek)){
            mWeek = "四";
        }else if("6".equals(mWeek)){
            mWeek = "五";
        }else if("7".equals(mWeek)){
            mWeek = "六";
        }
        timeText.setText(mHours + ":" + mMinute);
        dateText.setText(mMonth + "月" + mDay + "日" +"   " + "星期" + mWeek);
    }

    private void btnGetText(int id,RadioButton btn){

    }

    private int getNextWord(){
        Word nextWord;
        Word prevWord;

        List<Word> words = wordHelper.getRandXWords(1);
        Word w = words.get(0);
        wordText.setText(w.getWord());
        englishText.setText(w.getSoundmark());
        int id = w.getIndex();

        if(id == 0) {
             nextWord = wordHelper.getXWord(id+1);
             prevWord = wordHelper.getXWord(id+2);
        }else if(id == wordHelper.getWordsCount() - 1) {
             nextWord = wordHelper.getXWord(id-1);
             prevWord = wordHelper.getXWord(id-2);
        }else {
             nextWord = wordHelper.getXWord(id+1);
             prevWord = wordHelper.getXWord(id-1);
        }


        Random r = new Random();
        int random = r.nextInt(3);
        if(random == 0){
            radioOne.setText("A: "+ w.getChinese());
            radioTwo.setText("B: "+ nextWord.getChinese());
            radioThree.setText("C: "+ prevWord.getChinese());
        }else if(random == 1){
            radioOne.setText("A: "+ nextWord.getChinese());
            radioTwo.setText("B: "+ w.getChinese());
            radioThree.setText("C: "+ prevWord.getChinese());
        }else if(random == 2){
            radioOne.setText("A: "+ nextWord.getChinese());
            radioTwo.setText("B: "+ prevWord.getChinese());
            radioThree.setText("C: "+ w.getChinese());
        }
        return id;
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
