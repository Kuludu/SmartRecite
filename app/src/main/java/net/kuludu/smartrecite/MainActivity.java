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

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, SynthesizerListener {
    // 文本控件
    private TextView timeText, dateText, wordText, englishText;
    private ImageView playVioce; // 播放声音
    private String mMonth, mDay, mWeek, mHours, mMinute; // 显示时间
    private SpeechSynthesizer speechSynthesizer;   // 合成对象??
    // 锁屏
    private KeyguardManager km; // 锁屏
    private KeyguardManager.KeyguardLock kl; // 解锁
    private RadioGroup radioGroup; // 按钮组
    private RadioButton radioOne, radioTwo, radioThree; // 三个选项
    private SharedPreferences sharedPreferences;// 清量级数据库
    SharedPreferences.Editor editor = null;  // 编辑数据库

    int j = 0;
    List<Integer> list;   // 判断答了几个题
    // List<CET4Entity> datas; // 从数据库中获取词库

    // 手指滑动
    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    private SQLiteDatabase db; // 创建数据库

    // 此处省略一些操作数据库的对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 锁屏界面显示在手机的最上方
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        init_control();


    }

    public void init_control(){
        // 初始化全部的控件
        timeText = (TextView) findViewById(R.id.time_text);
        dateText = (TextView) findViewById(R.id.date_text);
        wordText = (TextView) findViewById(R.id.word_text);
        englishText = (TextView) findViewById(R.id.english_text);
        playVioce = (ImageView) findViewById(R.id.play_vioce);
        radioOne = (RadioButton) findViewById(R.id.choose_btn_one);
        radioTwo = (RadioButton) findViewById(R.id.choose_btn_two);
        radioThree = (RadioButton) findViewById(R.id.choose_btn_three);
        radioGroup = (RadioGroup) findViewById(R.id.choose_group);
        radioGroup.setOnCheckedChangeListener(this);
        playVioce.setOnClickListener(this);
        setParam();
        SpeechUser.getUser().login(MainActivity.this,null,null,"appid = 5fbbe4aa",speechListener);
    }

    public void init() {
        // 初始化数据库
        sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit(); // 初始化清凉级数据库编辑者
        list = new ArrayList<Integer>();
        // 初始化一个10个10以内的随机数
        Random r = new Random();
        while (list.size() < 10) {
            int i = r.nextInt();
            if (!list.contains(i)) {
                list.add(i);
            }
        }
        // 得到键盘锁管理的对象
        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unlock");
        // 对数据库的一系列操作初。。。。

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_vioce:
                // String text = wordText.getText().toString().trim();
                Toast.makeText(this,"发声中!",Toast.LENGTH_LONG).show();
                speechSynthesizer.startSpeaking("hello",this);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    /* 语音合成模块 */
    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {

    }

    // 回调用对象
    private SpeechListener speechListener = new SpeechListener() {
        @Override
        public void onEvent(int i, Bundle bundle) {

        }

        @Override
        public void onData(byte[] bytes) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }
    };

    /* 初始化语音播报 */
    public void setParam(){
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        speechSynthesizer.setParameter(SpeechConstant.SPEED,"50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME,"50");
        speechSynthesizer.setParameter(SpeechConstant.PITCH,"50");
    }
}
